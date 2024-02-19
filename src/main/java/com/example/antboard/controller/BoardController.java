package com.example.antboard.controller;

import com.example.antboard.dto.request.board.BoardDto;
import com.example.antboard.dto.request.board.BoardEditDto;
import com.example.antboard.dto.response.BoardListResponse;
import com.example.antboard.dto.response.board.BoardDetailResponseDto;
import com.example.antboard.dto.response.board.BoardResponseDto;
import com.example.antboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<BoardListResponse>> boardList(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardListResponse> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }
    @PostMapping("/write")
    public ResponseEntity<BoardResponseDto> write(
            @RequestBody BoardDto boardDto){
        BoardResponseDto write = boardService.save(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(write);
    }
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponseDto> getBoard(@PathVariable("boardId") Long boardId){
        BoardDetailResponseDto findBoard =boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoard);
    }
    //수정
    @PostMapping("/{boardId}/edit")
    public ResponseEntity<BoardDetailResponseDto> Edit(@PathVariable("boardId") Long boardId,
                                                       @RequestBody BoardEditDto dto){
        BoardDetailResponseDto board = boardService.update(boardId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }
    // 상세보기 -> 삭제
    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<Long> delete(@PathVariable("boardId") Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
