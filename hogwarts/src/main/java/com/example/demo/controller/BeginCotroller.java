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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BeginCotroller {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {

        return "main";
    }

    @GetMapping("/info/staff")
    public String info(HttpSession session, Model model) {
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        Users users = usersRepository.findById(authInfo.getId());
        model.addAttribute("user", users);

        return "/info/staff";
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
}
