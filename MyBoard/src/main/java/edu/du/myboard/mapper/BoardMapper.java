package edu.du.myboard.mapper;


import edu.du.myboard.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

	@Select("select count(*) from t_board")
	public int boardCount();

	@Select("select board_idx, title, hit_cnt, DATE_FORMAT(created_datetime, '%Y.%m.%d %H:%i:%s') AS created_datetime from t_board" +
			" WHERE deleted_yn = 'N' order by board_idx desc limit #{firstRow}, #{endRow}")
	public List<BoardDto> select(int firstRow, int endRow);

	List<BoardDto> selectBoardList() throws Exception;
	
	void insertBoard(BoardDto board) throws Exception;

	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	void updateHitCount(int boardIdx) throws Exception;
	
	void updateBoard(BoardDto board) throws Exception;
	
	void deleteBoard(int boardIdx) throws Exception;
}
