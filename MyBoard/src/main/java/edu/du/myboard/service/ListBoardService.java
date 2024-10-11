package edu.du.myboard.service;

import edu.du.myboard.dto.BoardDto;
import edu.du.myboard.dto.BoardListModel;
import edu.du.myboard.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListBoardService {

	@Autowired
	BoardMapper boardMapper;

	public static final int COUNT_PER_PAGE = 10;

	public BoardListModel getArticleList(int requestPageNumber) {
		if (requestPageNumber < 0) {
			throw new IllegalArgumentException("page number < 0 : "
					+ requestPageNumber);
		}
		int totalArticleCount = boardMapper.boardCount();

		if (totalArticleCount == 0) {
			return new BoardListModel();
		}

		int totalPageCount = calculateTotalPageCount(totalArticleCount);

		int firstRow = (requestPageNumber - 1) * COUNT_PER_PAGE + 1;
		int endRow = firstRow + COUNT_PER_PAGE - 1;

		if (endRow > totalArticleCount) {
			endRow = totalArticleCount;
		}
		List<BoardDto> articleList = boardMapper.select(firstRow, endRow);

		BoardListModel boardListView = new BoardListModel(
				articleList, requestPageNumber, totalPageCount, firstRow,
				endRow);
		return boardListView;
	}

	private int calculateTotalPageCount(int totalArticleCount) {
		if (totalArticleCount == 0) {
			return 0;
		}
		int pageCount = totalArticleCount / COUNT_PER_PAGE;
		if (totalArticleCount % COUNT_PER_PAGE > 0) {
			pageCount++;
		}
		return pageCount;
	}
}
