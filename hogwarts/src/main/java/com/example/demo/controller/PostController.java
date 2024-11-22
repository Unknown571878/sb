package com.example.demo.controller;

import com.example.demo.entity.Schedule;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.service.MessageDto;
import com.example.demo.service.PostService;
import com.example.demo.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;

    private final PostService postService;

    private final CommentRepository commentRepository;

    private final ScheduleRepository scheduleRepository;

    private final ScheduleService scheduleService;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    private List<String> generateDays(String year, String month) {
        List<String> days = new ArrayList<>();
        int totalDays = java.time.YearMonth.of(Integer.parseInt(year), Integer.parseInt(month)).lengthOfMonth();
        for (int i = 1; i <= totalDays; i++) {
            days.add(String.format("%02d", i)); // 두 자릿수로 일자를 맞추기
        }
        return days;
    }

    @GetMapping("/notice")
    public String notice(Model model, @PageableDefault(page=0,size=20) Pageable pageable) {
        List<Post> posts = postRepository.findByTypeOrderByPidDesc("notice");
        // 페이지 정보에 따라 현재 페이지의 시작 인덱스를 계산
        final int start = (int) pageable.getOffset();
        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int end = Math.min((start + pageable.getPageSize()), posts.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<Post> page = new PageImpl<>(posts.subList(start, end), pageable, posts.size());
        // 페이지 객체를 모델에 추가하여 뷰에서 접근 가능하도록 함
        model.addAttribute("posts", page);
        return "/post/notice";
    }

    @GetMapping("/postList")
    public String postList(Model model, @PageableDefault(page=0,size=20) Pageable pageable) {
        List<Post> posts = postRepository.findByTypeOrderByPidDesc("free");
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
        String currentYear = String.valueOf(java.time.LocalDate.now().getYear());
        String currentMonth = String.format("%02d", java.time.LocalDate.now().getMonthValue());

        // 달력에 표시할 일자 목록 생성
        List<String> days = generateDays(currentYear, currentMonth); // 달력 날짜 계산 함수

        // 일정 목록 가져오기
        List<Schedule> schedules = scheduleService.getSchedulesByDate(currentYear, currentMonth, "01"); // 예시로 1일의 일정만 조회

        // 모델에 데이터 추가
        model.addAttribute("days", days);
        model.addAttribute("schedules", schedules);

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
    @PostMapping("/{pid}/commentDelete")
    public String commentDelete(@PathVariable Long pid, Long cid, Model model) {
        commentRepository.deleteById(cid);
        MessageDto message = new MessageDto("삭제되었습니다.", "/post/postDetail?pid=" + pid, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
    @PostMapping("/commentUpdate")
    public String commentUpdate(@RequestParam String updateComment, @RequestParam Long pid,
                                @RequestParam Long cid, Model model) {
        Comment update = commentRepository.findById(cid).orElse(null);
        update.setContent(updateComment);
        commentRepository.save(update);
        MessageDto message = new MessageDto("수정되었습니다", "/post/postDetail?pid=" + pid, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(@RequestParam("pid") Long pid, Model model) {
        Post post = postRepository.findById(pid).orElse(null);
        model.addAttribute("post", post);
        return "/post/noticeDetail";
    }
}
