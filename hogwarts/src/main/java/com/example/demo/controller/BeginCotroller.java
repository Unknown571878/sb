package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.reopository.UsersRepository;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BeginCotroller {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        if(session.getAttribute("authInfo") != null) {
            AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
            Users users = usersRepository.findById(authInfo.getId());
            model.addAttribute("user", users);
        }
        return "main";
    }

    @GetMapping("/info/staff")
    public String info(HttpSession session, Model model, HttpServletRequest request) {
        if(session.getAttribute("authInfo") != null) {
            AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
            Users users = usersRepository.findById(authInfo.getId());
            model.addAttribute("user", users);
        } else {
            MessageDto message = new MessageDto("로그인이 필요한 서비스입니다", "/loginForm", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        return "/info/staff";
    }

    @GetMapping("/info/password")
    public String password() {
        return "/info/password";
    }

    @GetMapping("/info/grade")
    public String grade() {
        return "/info/grade";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/loginForm";
    }

    @PostMapping("/login")
    public String login(LoginCommand loginCommand, HttpSession session, Errors errors, HttpServletResponse response) {

        new LoginCommandValidator().validate(loginCommand, errors);
        if (errors.hasErrors()) {
            return "/loginForm";
        }
        try {
            AuthInfo authInfo = authService.authenticate(
                    loginCommand.getId(),
                    loginCommand.getPassword());
            session.setAttribute("authInfo", authInfo);

            Cookie rememberCookie = new Cookie("Remember", loginCommand.getId());
            rememberCookie.setPath("/");
            if (loginCommand.isRememberEmail()) {
                rememberCookie.setMaxAge(60 * 60 * 24 * 30);
            } else {
                rememberCookie.setMaxAge(0);
            }
            response.addCookie(rememberCookie);
            return "redirect:/";
        }catch (WrongIdPasswordException e) {
            errors.reject("idPasswordNotMatching");
            return "/loginForm";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/info/gradeInput")
    public String gradeInput(Model model) {
        return "/info/gradeInput";
    }
}
