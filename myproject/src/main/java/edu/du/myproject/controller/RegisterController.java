package edu.du.myproject.controller;

import edu.du.myproject.entity.Member;
import edu.du.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final MemberRepository memberRepository;

    @GetMapping
    public String register() {
        return "/register/step1";
    }

    @PostMapping("/newMember")
    public String newMember(Member member) {
        memberRepository.save(member);
        return "redirect:/post";
    }
}
