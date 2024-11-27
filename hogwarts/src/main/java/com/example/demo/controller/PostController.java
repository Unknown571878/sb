package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AuthInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.service.MessageDto;
import com.example.demo.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;

    private final PostService postService;

    private final CommentRepository commentRepository;

    private final ScheduleRepository scheduleRepository;

    private final InquiryRepository inquiryRepository;
    private final UsersRepository usersRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
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
    public String schedule(@RequestParam(required = false) Integer year,
                           @RequestParam(required = false) Integer month,
                           Model model) {
        // 현재 연도와 월 기본값 설정
        LocalDate currentDate = LocalDate.now();
        int currentYear = (year != null) ? year : currentDate.getYear();
        int currentMonth = (month != null) ? month : currentDate.getMonthValue();

        // 현재 월의 첫 날과 마지막 날 계산
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        // 첫 날의 요일 가져오기 (일요일 = 0, 월요일 = 1, ...)
        int startDayOfWeek = startOfMonth.getDayOfWeek().getValue() % 7;

        // 현재 월의 모든 날짜 생성
        List<Integer> days = new ArrayList<>();

        // 이전 달의 빈 칸 채우기
        for (int i = 0; i < startDayOfWeek; i++) {
            days.add(null); // null은 빈 칸으로 처리
        }

        // 현재 달의 날짜 추가
        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            days.add(i);
        }

        // 다음 달의 빈 칸 채우기 (7로 나눠 떨어지지 않을 경우)
        int remainingCells = 7 - (days.size() % 7);
        if (remainingCells < 7) {
            for (int i = 0; i < remainingCells; i++) {
                days.add(null);
            }
        }

        // 일정 가져오기
        List<Schedule> schedules = scheduleRepository.findByScheduleDateBetweenOrderByScheduleDate(startOfMonth, endOfMonth);

        // 이전/다음 월 계산
        LocalDate prevMonth = startOfMonth.minusMonths(1);
        LocalDate nextMonth = startOfMonth.plusMonths(1);

        // 모델 데이터 추가
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("prevYear", prevMonth.getYear());
        model.addAttribute("prevMonth", prevMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());
        model.addAttribute("days", days);
        model.addAttribute("schedules", schedules);

        return "post/schedule"; // HTML 템플릿 경로
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
        updatePost.setUpdateAt(LocalDateTime.now());
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
        post.setCnt(post.getCnt() + 1);
        postRepository.save(post);
        model.addAttribute("post", post);
        return "/post/noticeDetail";
    }

    @GetMapping("/inquiryList")
    public String inquiryList(Model model, @PageableDefault(page=0,size=20) Pageable pageable, HttpSession session) {
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        List<Inquiry> posts = inquiryRepository.findByTypeAndUidOrderByPidDesc("question", authInfo.getId());
        List<Inquiry> admin = inquiryRepository.findByTypeOrderByPidDesc("question");
        // 페이지 정보에 따라 현재 페이지의 시작 인덱스를 계산
        final int start = (int) pageable.getOffset();
        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int end = Math.min((start + pageable.getPageSize()), posts.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<Inquiry> page = new PageImpl<>(posts.subList(start, end), pageable, posts.size());
        // 페이지 객체를 모델에 추가하여 뷰에서 접근 가능하도록 함

        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int adminEnd = Math.min((start + pageable.getPageSize()), admin.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<Inquiry> adminPage = new PageImpl<>(admin.subList(start, adminEnd), pageable, admin.size());
        // 페이지 객체를 모델에 추가하여 뷰에서 접근 가능하도록 함

        model.addAttribute("adminPages", adminPage);
        model.addAttribute("inquirys", page);
        return "/post/inquiryList";
    }

    @GetMapping("/inquiryInsertForm")
    public String inquiryInsertForm() {
        return "/post/inquiryInsertForm";
    }

    @PostMapping("/inquiryInsert")
    public String inquiryInsert(@ModelAttribute Inquiry inquiry, Model model) {
        Inquiry newInquiry = Inquiry.builder()
                .uid(inquiry.getUid())
                .name(inquiry.getName())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .createAt(LocalDate.now())
                .type("question")
                .answer(false)
                .build();
        newInquiry = inquiryRepository.save(newInquiry);
        newInquiry.setPid(newInquiry.getId());
        inquiryRepository.save(newInquiry);
        MessageDto message = new MessageDto("등록되었습니다", "/post/inquiryList", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/inquiryDetail")
    public String inquiryDetail(@RequestParam("id") Long id, Model model) {
        List<Inquiry> detail = inquiryRepository.findByPidAndTypeOrderByCreateAtAsc(id, "answer");
        Inquiry main = inquiryRepository.findById(id).orElse(null);
        model.addAttribute("inquirys", detail);
        model.addAttribute("main", main);
        return "/post/inquiryDetail";
    }

    @GetMapping("/noticeInsertForm")
    public String noticeInsertForm() {
        return "/post/noticeInsertForm";
    }

    @PostMapping("/noticeInsertForm/{pid}")
    public String noticeInsertForm(@PathVariable Long pid, Model model) {
        Post post = postRepository.findById(pid).orElse(null);
        model.addAttribute("post", post);
        return "/post/noticeInsertForm";
    }

    @PostMapping("/noticeDelete/{pid}")
    public String noticeDelete(@PathVariable Long pid, Model model) {
        postService.deletePost(pid);
        MessageDto message = new MessageDto("삭제가 완료되었습니다", "/post/notice", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/noticeUpdate")
    public String noticeUpdate(@ModelAttribute Post newPost, Model model) {
        Post post = postRepository.findById(newPost.getPid()).orElse(null);
        post.setTitle(newPost.getTitle());
        post.setContent(newPost.getContent());
        post.setName(newPost.getName());
        post.setUpdateAt(LocalDateTime.now());
        postRepository.save(post);
        MessageDto message = new MessageDto("수정을 완료되었습니다", "/post/notice", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/noticeInsert")
    public String noticeInsert(@ModelAttribute Post post, Model model) {
        log.info(post.toString());
        post.setCreatedAt(LocalDateTime.now());
        post.setType("notice");
        postRepository.save(post);
        MessageDto message = new MessageDto("공지사항이 등록되었습니다", "/post/notice", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/answerInsert")
    public String answerInsert(@RequestParam Long id,
                               @RequestParam String content, Model model) {
        Inquiry inquiry = inquiryRepository.findById(id).orElse(null);
        Inquiry answer = Inquiry.builder()
                .pid(inquiry.getId())
                .title(inquiry.getTitle())
                .content(content)
                .name("관리자")
                .createAt(LocalDate.now())
                .type("answer")
                .answer(true)
                .build();
        inquiryRepository.save(answer);
        inquiry.setAnswer(true);
        inquiryRepository.save(inquiry);
        MessageDto message = new MessageDto("답글을 작성했습니다", "/post/inquiryDetail?id="+id, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/reQuestionInsert")
    public String reQuestionInsert(@RequestParam Long id,
                                   @RequestParam String content,
                                   Model model) {
        Inquiry inquiry = inquiryRepository.findById(id).orElse(null);
        Inquiry answer = Inquiry.builder()
                .pid(inquiry.getId())
                .title(inquiry.getTitle())
                .uid(inquiry.getUid())
                .content(content)
                .name(inquiry.getName())
                .createAt(LocalDate.now())
                .type("answer")
                .answer(true)
                .build();
        inquiryRepository.save(answer);
        MessageDto message = new MessageDto("답글을 작성했습니다", "/post/inquiryDetail?id="+id, RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
