package com.example.antboard.service;

import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.dto.request.comment.CommentDto;
import com.example.antboard.dto.response.comment.CommentResponseDto;
import com.example.antboard.entity.Board;
import com.example.antboard.entity.Comment;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.BoardRepository;
import com.example.antboard.repository.CommentRepository;
import com.example.antboard.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<CommentResponseDto> getAllComments(Pageable pageable, Long boardId) {
        Page<Comment> comments = commentRepository.findAllWithMemberAndBoard(pageable, boardId);
        List<CommentResponseDto> commentList = comments.getContent().stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }
    @Transactional
    public CommentResponseDto write(Long boardId,Member member,CommentDto commentDto) {
        // board 정보 검색
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board id", String.valueOf(boardId))
        );
        // member(댓글 작성자) 정보 검색
        Member commentWriter = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member id", String.valueOf(member.getEmail()))
        );
        // Entity 변환, 연관관계 매핑
        Comment comment = CommentDto.of(commentDto);
        comment.setBoard(board);
        comment.setMember(commentWriter);

        Comment saveComment = commentRepository.save(comment);
        return CommentResponseDto.from(saveComment);
    }
    @Transactional
    public CommentResponseDto update(Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdWithMemberAndBoard(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );
        comment.update(commentDto.getContent());
        return CommentResponseDto.from(comment);
    }
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);

    }
}
