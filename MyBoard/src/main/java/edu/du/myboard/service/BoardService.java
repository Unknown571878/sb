package edu.du.myboard.service;



import edu.du.myboard.dto.BoardDto;

import java.util.List;

public interface BoardService {
	public int boardCount();

	List<BoardDto> selectBoardList() throws Exception;

	void insertBoard(BoardDto board) throws Exception;

	BoardDto selectBoardDetail(int boardIdx) throws Exception;

	void updateBoard(BoardDto board) throws Exception;

	void deleteBoard(int boardIdx) throws Exception;
}
