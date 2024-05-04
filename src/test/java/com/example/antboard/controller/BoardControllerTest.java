package com.example.antboard.controller;

import com.example.antboard.dto.response.BoardListResponse;
import com.example.antboard.service.BoardService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Test
    void testBoardListPerformance() {
        MockitoAnnotations.openMocks(this);

        // Creating 100 pages with 10 items each
        int pageSize = 10;
        int totalItems = 1000;
        List<BoardListResponse> boardListResponses = new ArrayList<>();
        for (long i = 1; i <= totalItems; i++) {
            boardListResponses.add(new BoardListResponse(
                    i,
                    "Title " + i,
                    "Content " + i,
                    LocalDateTime.now().toString(),
                    LocalDateTime.now().toString(),
                    Collections.singletonList("base64data " + i)
            ));
        }

        Pageable pageable = PageRequest.of(0, pageSize);
        Page<BoardListResponse> page = new PageImpl<>(boardListResponses.subList(0, pageSize), pageable, totalItems);
        when(boardService.getAllBoards(pageable)).thenReturn(page);

        // Testing for all 100 pages
        long startTime = System.nanoTime();
        for (int pageIndex = 0; pageIndex < 100; pageIndex++) {
            pageable = PageRequest.of(pageIndex, pageSize);
            when(boardService.getAllBoards(pageable)).thenReturn(
                    new PageImpl<>(
                            boardListResponses.subList(pageIndex * pageSize, (pageIndex + 1) * pageSize),
                            pageable,
                            totalItems
                    )
            );

            ResponseEntity<Page<BoardListResponse>> response = boardController.boardList(pageable);
            Page<BoardListResponse> result = response.getBody();

            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(totalItems);
            assertThat(result.getContent().size()).isEqualTo(pageSize);
        }
        long endTime = System.nanoTime();
        long durationInMilliseconds = (endTime - startTime) / 1_000_000;

        // Assert that the entire test execution took less than 2000 milliseconds (2 seconds)
        assertTrue(durationInMilliseconds < 2000, "Performance test took too long: " + durationInMilliseconds + "ms");
    }
}