package com.example.antboard.service;

import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.dto.request.board.BoardDto;
import com.example.antboard.dto.request.board.BoardEditDto;
import com.example.antboard.dto.response.BoardListResponse;
import com.example.antboard.dto.response.board.BoardDetailResponseDto;
import com.example.antboard.dto.response.board.BoardResponseDto;
import com.example.antboard.dto.response.file.BoardDetailsFileResponseDto;
import com.example.antboard.entity.Board;
import com.example.antboard.entity.FileEntity;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.BoardRepository;
import com.example.antboard.repository.FileRepository;
import com.example.antboard.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;
    //게시글 페이징 리스트
    @Transactional
    public Page<BoardListResponse> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        List<BoardListResponse> list = boards.getContent().stream()
                .map(board -> {
                    List<String> imageBase64Data = fileRepository.findByBoard(board).stream()
                            .map(FileEntity::getBase64Data)
                            .collect(Collectors.toList());
                    return new BoardListResponse(board, imageBase64Data); // 이미지 데이터 포함하여 생성
                })
                .collect(Collectors.toList());
        return new PageImpl<>(list,pageable, boards.getTotalElements());
    }
    @Transactional//게시글 가져오기
    public BoardDetailResponseDto getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 " + boardId));
        List<BoardDetailsFileResponseDto> files = fileRepository.findByBoard(board).stream()
                .map(file -> BoardDetailsFileResponseDto.builder()
                        .fileId(file.getId())
                        .FileName(file.getFileName())
                        .fileType(file.getFileType())
                        .imageBase64Data(file.getBase64Data()) // Base64 인코딩된 데이터 직접 전달
                        .build()).collect(Collectors.toList());

        return BoardDetailResponseDto.from(board, files);
    }

    // 게시글 등록
    @Transactional
    public BoardResponseDto save(BoardDto dto, Member member){
        Board board = BoardDto.ofEntity(dto);
//        Member writerMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
//                () -> new ResourceNotFoundException("Member", "Member Email", member.getEmail())
//        );
        Member writerMember = getMember();
        board.setMappingMember(writerMember);
        Board newBoard = boardRepository.save(board);
        return BoardResponseDto.from(newBoard, writerMember.getUsername());
    }

    private Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getName();
        return memberRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("Member", "Member Email",  authentication.getName()));
    }

    // 게시글 수정
    @Transactional
    public BoardDetailResponseDto update(Long boardId, BoardEditDto dto){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당게시글은 없습니다. id" + boardId));
        board.update(dto.getTitle(), board.getContent());
        List<BoardDetailsFileResponseDto> files = fileRepository.findByBoard(board).stream()
                .map(file -> BoardDetailsFileResponseDto.builder()
                        .fileId(file.getId())
                        .FileName(file.getFileName())
                        .fileType(file.getFileType())
                        .imageBase64Data(List.of(file.getBase64Data()).toString())
                        .build()).collect(Collectors.toList());
        return BoardDetailResponseDto.from(board,files);
    }
    // 게시글 삭제
    @Transactional
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }

}
