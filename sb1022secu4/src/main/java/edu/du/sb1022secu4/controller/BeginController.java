package edu.du.sb1022secu4.controller;

import edu.du.sb1022secu4.entity.Member;
import edu.du.sb1022secu4.repository.MemberRepository;
import edu.du.sb1022secu4.spring.DuplicateMemberException;
import edu.du.sb1022secu4.spring.MemberRegisterService;
import edu.du.sb1022secu4.spring.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class BeginController {

    private PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberRegisterService memberRegisterService;

    @GetMapping("/")
    public String index() {
        return "sample/all";
    }

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .id(1001L)
                .name("홍길동")
                .password(passwordEncoder().encode("1234"))
                .email("hong@aaa.com")
                .build();
        memberRepository.save(member);
    }

    @RequestMapping("/register/step1")
    public String handleStep1() {
        return "register/step1";
    }

    @GetMapping("/register/step2")
    public String handleStep2(
            @RequestParam(value = "agree", defaultValue = "false") Boolean agree,
            Model model) {
        if (!agree) {
            return "register/step1";
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register/step2";
    }

//    @GetMapping("/register/step2")
//    public String handleStep2Get() {
//        return "redirect:/register/step1";
//    }

    @GetMapping("/register/step3")
    public String handleStep3(RegisterRequest regReq, Model model) {
        try {
            memberRegisterService.regist(regReq);
            model.addAttribute("registerResponse", regReq.getName());
            return "register/step3";
        } catch (DuplicateMemberException ex) {
            return "register/step2";
        }
    }
}
