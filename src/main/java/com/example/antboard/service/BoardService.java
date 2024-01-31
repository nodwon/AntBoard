package com.example.antboard.service;

import com.example.antboard.dto.request.board.BoardDto;
import com.example.antboard.dto.request.board.BoardEditDto;
import com.example.antboard.dto.response.BoardResponseDto;
import com.example.antboard.entity.Board;
import com.example.antboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    // 게시글 등록
    public BoardResponseDto save(BoardDto dto){
        Board board = BoardDto.ofEntity(dto);
        Board newBoard = boardRepository.save(board);
        return BoardResponseDto.from(newBoard);
    }
    // 게시글 수정
    public Long update(Long boardId, BoardEditDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당게시글은 없습니다. id" + boardId));
        board.update(dto.getTitle(), board.getContent());
        return board.getId();
    }
    // 게시글 삭제
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
