package org.example.owncalendarserver.service;

import lombok.RequiredArgsConstructor;
import org.example.owncalendarserver.dto.CommentRequestDto;
import org.example.owncalendarserver.dto.CommentResponseDto;
import org.example.owncalendarserver.entity.Comment;
import org.example.owncalendarserver.entity.Schedule;
import org.example.owncalendarserver.repository.CommentRepository;
import org.example.owncalendarserver.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new IllegalArgumentException("선택한 일정은 존재하지 않습니다.")
        );

        Comment comment = new Comment(requestDto, schedule);

        Comment saveComment = commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);

        return commentResponseDto;
    }

    @Transactional
    public CommentResponseDto editComment(CommentRequestDto requestDto, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
        );

        checkId(comment, requestDto);

        comment.update(requestDto);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);

        return  commentResponseDto;
    }

    public void checkId(Comment comment, CommentRequestDto requestDto) {
      if(!comment.getUser_id().equals(requestDto.getUser_id())) {
          throw new IllegalStateException("사용자가 일치하지 않습니다.");
      }
    }
}