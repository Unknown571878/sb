package com.example.demo.controller;

import com.example.demo.entity.Post;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Users;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BeginCotroller {

    private final UsersRepository usersRepository;
    private final AuthService authService;
    private final GradeRepository gradeRepository;
    private final PostRepository postRepository;
    private final ScheduleRepository scheduleRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        List<Post> posts = postRepository.findByTypeOrderByPidDesc("notice");
        model.addAttribute("posts", posts);

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        // 현재 월의 첫 날과 마지막 날 계산
        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        List<Schedule> schedules = scheduleRepository.findByScheduleDateAfterOrderByScheduleDate(startOfMonth);
        model.addAttribute("schedules", schedules);

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


    @GetMapping("/loginForm")
    public String loginForm() {
        return "/loginForm";
    }

    @PostMapping("/login")
    public String login(LoginCommand loginCommand, HttpSession session, Errors errors, HttpServletResponse response, Model model) {

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
            MessageDto message = new MessageDto("로그인에 실패하였습니다", "/loginForm", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/info/newPassword")
    public String newPassword(@RequestParam String password, @RequestParam Long uid, Model model) {
        Users user = usersRepository.findById(uid).orElse(null);
        if(password.equals(user.getPassword())) {
            return "/info/newPassword";
        } else {
            MessageDto message = new MessageDto("비밀번호가 다릅니다.", "/info/password", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @PostMapping("/info/newPasswordInsert")
    public String newPasswordInsert(@RequestParam String newPassword, @RequestParam String passwordConfirm, @RequestParam Long uid, Model model) {
        if (newPassword.equals(passwordConfirm)) {
            Users user = usersRepository.findById(uid).orElse(null);
            user.setPassword(newPassword);
            usersRepository.save(user);
            MessageDto message = new MessageDto("비밀번호가 변경되었습니다.", "/", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        } else {
            MessageDto message = new MessageDto("비밀번호가 일치하지 않습니다.", "/info/newPassword", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @GetMapping("/InfoUpdateForm")
    public String update(HttpSession session, Model model) {
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        Users users = usersRepository.findById(authInfo.getId());
        model.addAttribute("user", users);
        return "/info/updateInfoForm";
    }

    @PostMapping("/InfoUpdate")
    public String infoUpdate(HttpSession session, Users user, Model model) {
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        Users users = usersRepository.findById(authInfo.getId());
        users.setName(user.getName());
        users.setAddress(user.getAddress());
        users.setEmail(user.getEmail());
        users.setPhone(user.getPhone());
        usersRepository.save(users);
        MessageDto message = new MessageDto("정보가 수정되었습니다", "/info/staff", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

}
