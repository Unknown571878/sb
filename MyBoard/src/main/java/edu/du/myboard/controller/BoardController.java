package edu.du.myboard.controller;


import edu.du.myboard.dto.BoardDto;
import edu.du.myboard.dto.BoardListModel;
import edu.du.myboard.dto.CommentDto;
import edu.du.myboard.service.BoardService;
import edu.du.myboard.service.CommentService;
import edu.du.myboard.service.ListBoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ListBoardService listBoardService;

	@GetMapping("/")
	public String index(){
		return "redirect:/board/openBoardList.do";
	}
	
	@GetMapping("/board/openBoardList.do")
	public String openBoardList(HttpServletRequest request, Model model) throws Exception{

		String pageNumberString = request.getParameter("p");
		int pageNumber = 1;
		if (pageNumberString != null && pageNumberString.length() > 0) {
			pageNumber = Integer.parseInt(pageNumberString);
		}
		BoardListModel boardListModel = listBoardService.getArticleList(pageNumber);
		request.setAttribute("listModel", boardListModel);

		if (boardListModel.getTotalPageCount() > 0) {
			int beginPageNumber =
					(boardListModel.getRequestPage() - 1) / 10 * 10 + 1;
			int endPageNumber = beginPageNumber + 9;
			if (endPageNumber > boardListModel.getTotalPageCount()) {
				endPageNumber = boardListModel.getTotalPageCount();
			}
			request.setAttribute("beginPage", beginPageNumber);
			request.setAttribute("endPage", endPageNumber);
		}
		
		List<BoardDto> list = boardService.selectBoardList();
		model.addAttribute("list", list);
		
		return "/board/boardList";
	}
	
	@RequestMapping("/board/openBoardWrite.do")
	public String openBoardWrite() throws Exception{
		return "/board/boardWrite";
	}
	
	@RequestMapping("/board/insertBoard.do")
	public String insertBoard(BoardDto board) throws Exception{
		boardService.insertBoard(board);
		return "redirect:/board/openBoardList.do";
	}
	
	@GetMapping("/board/openBoardDetail.do")
	public String openBoardDetail(@RequestParam int boardIdx, Model model) throws Exception{
		
		BoardDto board = boardService.selectBoardDetail(boardIdx);
		model.addAttribute("board", board);

		if (commentService.BoardComments(boardIdx) != null){
			List<CommentDto> comment = commentService.BoardComments(boardIdx);
			model.addAttribute("comment", comment);
		} else {
			model.addAttribute("comment", 0);
		}
		
		return "/board/boardDetail";
	}
	
	@RequestMapping("/board/updateBoard.do")
	public String updateBoard(BoardDto board) throws Exception{
		boardService.updateBoard(board);
		return "redirect:/board/openBoardList.do";
	}
	
	@RequestMapping("/board/deleteBoard.do")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList.do";
	}

	@PostMapping("/comments/add")
	public String addComment(CommentDto comment) throws Exception{
		commentService.insertComment(comment);
		return "redirect:/board/openBoardDetail.do?boardIdx=" + comment.getBoardIdx();
	}
}
