package edu.du.myboard.mapper;

import edu.du.myboard.dto.CommentDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("select * from t_board_comment where board_idx = #{boardIdx}")
    List<CommentDto> BoardComments(int boardIdx);

    @Insert("insert into t_board_comment(board_idx, contents, created_datetime, creator_id)" +
            "values (#{boardIdx}, #{c_contents}, now(), 'admin')")
    void insertComment(CommentDto commentDto);


    void updateComment(CommentDto commentDto);

    void deleteComment(int commentId);
}
