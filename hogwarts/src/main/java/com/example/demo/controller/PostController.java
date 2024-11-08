package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.reopository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("/notice")
    public String notice() {
        return "/post/notice";
    }

    @GetMapping("/postList")
    public String postList(Model model, @PageableDefault(page=0,size=20) Pageable pageable) {
        List<Post> posts = postRepository.findAllByOrderByPidDesc();
        // 페이지 정보에 따라 현재 페이지의 시작 인덱스를 계산
        final int start = (int) pageable.getOffset();
        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int end = Math.min((start + pageable.getPageSize()), posts.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<Post> page = new PageImpl<>(posts.subList(start, end), pageable, posts.size());
        // 페이지 객체를 모델에 추가하여 뷰에서 접근 가능하도록 함
        model.addAttribute("posts", page);
        log.info(page.toString());
        return "/post/postList";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        LocalDate now = LocalDate.now();
        int getMonth = now.getMonth().getValue();
        String month = getMonth + "월";
        model.addAttribute("month", month);
        return "/post/schedule";
    }
}
