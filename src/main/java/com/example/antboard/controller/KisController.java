package com.example.antboard.controller;

import com.example.antboard.entity.KisIndexData;
import com.example.antboard.service.KisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/kis")
@RequiredArgsConstructor
public class KisController {
    private static final Logger log = LoggerFactory.getLogger(KisController.class);
    private final KisService kisService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }


    @GetMapping("/indices")
    public Mono<String> majorIndices(Model model) {
        List<Tuple2<String, String>> indicesParams = Arrays.asList(
                Tuples.of("0001", "U"),
                Tuples.of("2001", "U"),
                Tuples.of("1001", "U")
        );

        return Flux.fromIterable(indicesParams)
                .concatMap(tuple -> kisService.getMajorIndex(tuple.getT1(), tuple.getT2()))
                .map(jsonData -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.readValue(jsonData, KisIndexData.class);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to parse JSON data: {}", e.getMessage());
                        throw new RuntimeException("JSON parsing error", e);
                    }
                })
                .collectList()
                .doOnNext(indicesList -> {
                    model.addAttribute("indicesKor", indicesList);
                    model.addAttribute("jobDate", kisService.getJobDateTime());
                })
                .thenReturn("indices")
                .doOnError(e -> log.error("Error processing majorIndices: {}", e.getMessage()));

    }

    @GetMapping("/equities/{id}")
    public Mono<String> currentPrice(@PathVariable("id") String id, Model model) {
        return kisService.getCurrentPrice(id)
                .doOnSuccess(body -> {
                    model.addAttribute("equity", body.getOutput());
                    model.addAttribute("jobDate", kisService.getJobDateTime());
                })
                .doOnError(e -> {
                    log.error("Error fetching equity details for ID {}: {}", id, e.getMessage());
                })
                .map(success -> "equities") // 반환할 뷰 이름 명시
                .onErrorReturn("error");   // 에러 발생 시 표시할 뷰
    }
}
