package edu.du.myboard.service;

import edu.du.myboard.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> BoardComments(int boardIdx);

    void insertComment(CommentDto commentDto);

    void updateComment(CommentDto commentDto);

    void deleteComment(int commentId);
}
