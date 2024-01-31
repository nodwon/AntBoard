package com.example.antboard.controller;

import com.example.antboard.dto.request.board.BoardDto;
import com.example.antboard.dto.request.board.BoardEditDto;
import com.example.antboard.dto.response.BoardListResponse;
import com.example.antboard.dto.response.BoardResponseDto;
import com.example.antboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    // 페이징 목록
    @GetMapping("board/list")
    public ResponseEntity<Page<BoardListResponse>> boardList(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardListResponse> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }
    @PostMapping("board/write")
    public ResponseEntity<BoardResponseDto> write(
            @RequestBody BoardDto boardDto){
        BoardResponseDto write = boardService.save(boardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(write);
    }
    @GetMapping("board/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId){
        BoardResponseDto findBoard =boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoard);
    }
    //수정
    @PostMapping("board/{boardId}/edit")
    public ResponseEntity<Long> Edit(@PathVariable Long boardId,
                                                 @RequestBody BoardEditDto dto){
        Long board = boardService.update(boardId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }
    // 상세보기 -> 삭제
    @PostMapping("board/{boardId}/delete")
    public ResponseEntity<Long> delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
