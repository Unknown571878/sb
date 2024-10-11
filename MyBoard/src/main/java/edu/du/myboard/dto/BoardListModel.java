package edu.du.myboard.dto;



import java.util.Collections;
import java.util.List;

public class BoardListModel {

	private List<BoardDto> BoardList;
	private int requestPage;
	private int totalPageCount;
	private int startRow;
	private int endRow;

	public BoardListModel() {
		this(Collections.<BoardDto>emptyList(), 0, 0, 0, 0);
	}

	public BoardListModel(List<BoardDto> BoardList, int requestPageNumber,
                          int totalPageCount, int startRow, int endRow) {
		this.BoardList = BoardList;
		this.requestPage = requestPageNumber;
		this.totalPageCount = totalPageCount;
		this.startRow = startRow;
		this.endRow = endRow;
	}

	public List<BoardDto> getBoardList() {
		return BoardList;
	}

	public int getRequestPage() {
		return requestPage;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}
}
