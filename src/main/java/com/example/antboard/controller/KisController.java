package com.example.antboard.controller;

import com.example.antboard.entity.KisIndexData;
import com.example.antboard.service.KisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Arrays;
import java.util.List;

@Controller("/kis")
public class KisController {
    private final KisService kisService;

    @Autowired
    public KisController(KisService kisService) {
        this.kisService = kisService;
    }
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }


    @GetMapping("/indices")
    public String majorIndices(Model model) {
        List<Tuple2<String, String>> iscdsAndOtherVariable1 = Arrays.asList(
                Tuples.of("0001", "U"),
                Tuples.of("2001", "U"),
                Tuples.of("1001", "U")
        );

        Flux<KisIndexData> indicesFlux = Flux.fromIterable(iscdsAndOtherVariable1)
                .concatMap(tuple -> kisService.getMajorIndex(tuple.getT1(), tuple.getT2()))
                .map(jsonData -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        return objectMapper.readValue(jsonData, KisIndexData.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

        List<KisIndexData> indicesList = indicesFlux.collectList().block();
        model.addAttribute("indicesKor", indicesList);
        model.addAttribute("jobDate", kisService.getJobDateTime());

        return "indices";
    }

    @GetMapping("/equities/{id}")
    public Mono<String> currentPrice(@PathVariable("id") String id, Model model) {
        return kisService.getCurrentPrice(id)
                .doOnSuccess(body -> {
                    model.addAttribute("equity", body.getOutput());
                    model.addAttribute("jobDate", kisService.getJobDateTime());
                })
                .doOnError(result -> System.out.println("*** error: " + result))
                .thenReturn("equities");
    }
}
