package edu.du.sb1030.controller;

import edu.du.sb1030.exception.WrongIdPasswordException;
import edu.du.sb1030.member.AuthInfo;
import edu.du.sb1030.service.AuthInfoService;
import edu.du.sb1030.validation.LoginCommand;
import edu.du.sb1030.validation.LoginCommandValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class MainController {

    private final AuthInfoService authInfoService;

    @GetMapping("/main")
    public void main() {

    }

    @GetMapping("/admin")
    public void admin() {

    }

    @GetMapping("/login")
    public void login() {
    }

    @PostMapping("/loginaction")
    public String loginaction(LoginCommand loginCommand, Errors errors, HttpSession session) {
        new LoginCommandValidator().validate(loginCommand, errors);
        if(errors.hasErrors()) {
            return "redirect:/view/login";
        }
        try {
            AuthInfo authInfo = authInfoService.authenticate(loginCommand.getEmail(), loginCommand.getPassword());

            session.setAttribute("authInfo", authInfo);
            return "redirect:/view/loginSuccess";
        } catch (WrongIdPasswordException e) {
            errors.reject("idPasswordNotMatching");
            return "redirect:/view/login";
        }
    }
}
