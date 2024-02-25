package com.example.antboard.service;

import com.example.antboard.dto.request.board.BoardDto;
import com.example.antboard.dto.request.board.BoardEditDto;
import com.example.antboard.dto.response.BoardListResponse;
import com.example.antboard.dto.response.board.BoardDetailResponseDto;
import com.example.antboard.dto.response.board.BoardResponseDto;
import com.example.antboard.entity.Board;
import com.example.antboard.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    //게시글 페이징 리스트
    @Transactional
    public Page<BoardListResponse> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        List<BoardListResponse> list =boards.getContent().stream()
                .map(BoardListResponse::from)
                .toList();
        return new PageImpl<>(list,pageable, boards.getTotalElements());
    }

    //게시글 가져오기
    @Transactional
    public BoardDetailResponseDto getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다"+boardId));
        return BoardDetailResponseDto.from(board);
    }
    // 게시글 등록
    @Transactional
    public BoardResponseDto save(BoardDto dto){
        Board board = BoardDto.ofEntity(dto);
        Board newBoard = boardRepository.save(board);
        return BoardResponseDto.from(newBoard);
    }
    // 게시글 수정
    @Transactional
    public BoardDetailResponseDto update(Long boardId, BoardEditDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당게시글은 없습니다. id" + boardId));
        board.update(dto.getTitle(), board.getContent());
        return BoardDetailResponseDto.from(board);
    }
    // 게시글 삭제
    @Transactional
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

}
