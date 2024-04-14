package com.example.antboard.controller;

import com.example.antboard.dto.request.comment.CommentDto;
import com.example.antboard.dto.response.comment.CommentResponseDto;
import com.example.antboard.entity.Member;
import com.example.antboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/list")
    public ResponseEntity<Page<CommentResponseDto>> commentList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/write")
    public ResponseEntity<CommentResponseDto> write(
            @PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal(errorOnInvalidType=true)  Member member,
            @RequestBody CommentDto commentDto) {


        CommentResponseDto saveCommentDTO = commentService.write(boardId, member, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    @PostMapping("/update/{commentId}")
    public ResponseEntity<CommentResponseDto> update(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto) {

        CommentResponseDto updateCommentDTO = commentService.update(commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long commentId) {

        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
