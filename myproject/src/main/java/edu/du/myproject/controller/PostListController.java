package edu.du.myproject.controller;

import edu.du.myproject.entity.PostList;
import edu.du.myproject.repository.PostListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostListController {

    private final PostListRepository postListRepository;

    @GetMapping
    public String index(Model model, @PageableDefault(page=0,size=20) Pageable pageable) {
        List<PostList> posts = postListRepository.findAllByOrderByPidDesc();
        // 페이지 정보에 따라 현재 페이지의 시작 인덱스를 계산
        final int start = (int) pageable.getOffset();
        // 현재 페이지의 끝 인덱스를 계산하되, 목록 크기를 초과하지 않도록 함
        final int end = Math.min((start + pageable.getPageSize()), posts.size());
        // 현재 페이지의 아이템 서브리스트를 포함하는 Page 객체 생성
        final Page<PostList> page = new PageImpl<>(posts.subList(start, end), pageable, posts.size());
        // 페이지 객체를 모델에 추가하여 뷰에서 접근 가능하도록 함
        model.addAttribute("posts", page);
        log.info(page.toString());
        return "/post/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("pid") Long id, Model model) {
        Optional<PostList> post = postListRepository.findById(id);
        model.addAttribute("post", post.get());
        return "/post/detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("pid") Long id) {
        postListRepository.deleteById(id);
        return "redirect:/post";
    }

    @GetMapping("/edit")
    public String edit() {
        return "/post/edit";
    }

    @PostMapping("/insert")
    public String insert(PostList post) {
        postListRepository.save(post);
        return "redirect:/post";
    }
}
