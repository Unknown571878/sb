package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.reopository.PostRepository;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;

    private final PostService postService;

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

    @GetMapping("/postInsertForm")
    public String postInsertForm() {
        return "/post/postInsertForm";
    }

    @PostMapping("/postInsert")
    public String postInsert(@ModelAttribute Post post, HttpSession session) {

        log.info(post.toString());

        Post newPost = Post.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(LocalDateTime.now())
                .cnt(0)
                .name("admin")
                .build();
        postRepository.save(newPost);

        return "redirect:/postList";
    }

    @GetMapping("/postDetail")
    public String postDetail(@RequestParam("pid") Long pid, Model model) {
        Post post = postRepository.findById(pid).orElse(null);
        model.addAttribute("post", post);
        log.info(post.toString());
        return "/post/postDetail";
    }

    @PostMapping("/{pid}/commentAdd")
    public String addComment(@PathVariable Long pid, @RequestParam String content) {
        // 댓글 추가 로직
        postService.addCommentToPost(pid, content);
        return "redirect:/post/postList?pid=" + pid; // 댓글 작성 후 다시 게시글 상세보기로 리다이렉트
    }
}
