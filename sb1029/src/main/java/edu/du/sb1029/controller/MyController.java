package edu.du.sb1029.controller;

import edu.du.sb1029.entity.Notice;
import edu.du.sb1029.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyController {

    final NoticeRepository noticeRepository;

    @GetMapping("/")
    public String index() {
        return "redirect:/board/openBoardList.do";
    }

    @GetMapping("/board/openBoardList.do")
    public String openBoardList(Model model, @PageableDefault(page=0,size = 10) Pageable pageable) {
        List<Notice> list = noticeRepository.findAllByOrderByNoticeIdDesc(pageable);
        // 페이지 정보에 따라 현재 페이지의 시작 인덱스를 계산
        final int start = (int) pageable.getOffset();
        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<Notice> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        log.info(page.toString());
        model.addAttribute("list", page);

        return "/board/boardList";
    }

    @GetMapping("/board/openBoardWrite.do")
    public String openBoardWrite() {
        return "board/boardWrite";
    }

    @PostMapping("/board/insertBoard.do")
    public String insertBoard(@Valid Notice notice, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/form"; // 오류가 있는 경우 폼 페이지로 돌아가기
        }
        noticeRepository.save(notice);
        return "redirect:/board/openBoardList.do";
    }

    @GetMapping("board/openBoardDetail.do")
    public String openBoardDetail(Long noticeId, Model model) {
        Optional<Notice> board = noticeRepository.findById(noticeId);
        model.addAttribute("board", board.get());
        return "board/boardDetail";
    }

    @GetMapping("board/deleteBoard.do")
    public String deleteBoard(Long noticeId) {
        noticeRepository.deleteById(noticeId);
        return "redirect:/board/openBoardList.do";
    }
}
