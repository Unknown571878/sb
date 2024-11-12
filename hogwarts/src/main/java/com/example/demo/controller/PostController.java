package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.reopository.PostRepository;
import com.example.demo.service.MessageDto;
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

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

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
    @PostMapping("/postInsertForm/{pid}")
    public String postInsertForm(@PathVariable Long pid, Model model) {
        Post post = postRepository.findById(pid).orElse(null);
        model.addAttribute("post", post);
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
                .name(post.getName())
                .userid(post.getUserid())
                .build();
        postRepository.save(newPost);

        return "redirect:/post/postList";
    }

    @GetMapping("/postDetail")
    public String postDetail(@RequestParam("pid") Long pid, Model model) {
        Post post = postRepository.findById(pid).orElse(null);
        int cntUp = post.getCnt() + 1;
        post.setCnt(cntUp);
        postRepository.save(post);
        model.addAttribute("post", post);
        return "/post/postDetail";
    }

    @PostMapping("/{pid}/commentAdd")
    public String addComment(@PathVariable Long pid, @RequestParam String content, @RequestParam String name, @RequestParam String id,
                             @RequestParam Long uid) {
        // 댓글 추가 로직
        postService.addCommentToPost(pid, id, name ,content, uid);
        return "redirect:/post/postDetail?pid=" + pid; // 댓글 작성 후 다시 게시글 상세보기로 리다이렉트
    }

    @PostMapping("/postDelete/{pid}")
    public String postDelete(@PathVariable Long pid, Model model) {
        postService.deletePost(pid);
        MessageDto message = new MessageDto("삭제가 완료되었습니다", "/post/postList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/postUpdate")
    public String postUpdate(@ModelAttribute Post post, Model model) {
        Post updatePost = postRepository.findById(post.getPid()).orElse(null);
        updatePost.setTitle(post.getTitle());
        updatePost.setContent(post.getContent());
        updatePost.setPid(post.getPid());
        postRepository.save(updatePost);
        MessageDto message = new MessageDto("수정되었습니다", "/post/postList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
