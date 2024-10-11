package edu.du.myboard.service;

import edu.du.myboard.dto.CommentDto;
import edu.du.myboard.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServicempl implements CommentService{

    @Autowired
    CommentMapper commentMapper;

    @Override
    public List<CommentDto> BoardComments(int boardIdx) {
        return commentMapper.BoardComments(boardIdx);
    }

    @Override
    public void insertComment(CommentDto commentDto) {
        commentMapper.insertComment(commentDto);
    }

    @Override
    public void updateComment(CommentDto commentDto) {
        commentMapper.updateComment(commentDto);
    }

    @Override
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }
}
