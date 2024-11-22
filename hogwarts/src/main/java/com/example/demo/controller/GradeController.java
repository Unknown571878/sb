package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Grade;
import com.example.demo.entity.Registration;
import com.example.demo.entity.Users;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.GradeRepository;
import com.example.demo.repository.RegistrationRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.AuthInfo;
import com.example.demo.service.GradeService;
import com.example.demo.service.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeRepository gradeRepository;
    private final GradeService gradeService;
    private final RegistrationRepository registrationRepository;
    private final DepartmentRepository departmentRepository;
    private final UsersRepository usersRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/info/grade")
    public String grade(@RequestParam(required = false) String year,
                        @RequestParam(required = false) String term,
                        HttpSession session, Model model) {
        if(session.getAttribute("authInfo") != null) {
            AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
            List<String> years = gradeService.getAllYears(authInfo.getId()); // 접속한 학생의 데이터를 검색해 데이터가 존재하는 학기와 학년을 가져옴
            List<String> terms = gradeService.getAllTerms(authInfo.getId());
            model.addAttribute("years", years);
            model.addAttribute("terms", terms);

            if (year != null && term != null) {
                List<Grade> grades = gradeService.getGradesByYearAndTerm(year, term, authInfo.getId());
                List<Map<String, Object>> stats = gradeService.calculateStatistics(authInfo.getId());

                model.addAttribute("grades", grades);
                model.addAttribute("yearTermStats", stats);
            }
            Map<String, Double> overallStats = gradeService.calculateOverallStatistics(authInfo.getId());
            model.addAttribute("overallStats", overallStats);
        } else {
            MessageDto message = new MessageDto("로그인이 필요한 서비스입니다", "/loginForm", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        return "/info/grade";
    }

    @GetMapping("/info/gradeList")
    public String gradeList(Model model, HttpSession session){
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        List<Department> departments = departmentRepository.findByProfessor(authInfo.getId());
        model.addAttribute("departments", departments);
        return "/info/gradeList";
    }

    @GetMapping("/info/gradeInput")
    public String gradeInput(@RequestParam("code") String code, Model model) {
        List<Registration> registrations = registrationRepository.findByCode(code);
        List<Users> users = new ArrayList<>();
        for (Registration registration : registrations) {
            users.add(usersRepository.findById(registration.getSid()));
        }
        Department department = departmentRepository.findByCode(code);
        if(department.isEvaluation())
        {
            MessageDto message = new MessageDto("이미 성적 입력이 완료되었습니다", "/info/gradeList", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        model.addAttribute("users", users);
        model.addAttribute("department", department);
        return "/info/gradeInput";
    }

    @PostMapping("/info/submitGrades")
    public String submitGrades(@RequestParam Map<String, String> scores, @RequestParam String code, Model model) {
        scores.forEach((key, value) -> {
            if (key.startsWith("scores[")) {
                String studentId = key.substring(7, key.length() - 1);
                Users users = usersRepository.findById(studentId);
                Department department = departmentRepository.findByCode(code);
                LocalDate currentDate = LocalDate.now();
                int year = currentDate.getYear();
                int month = currentDate.getMonthValue();
                Grade grade = Grade.builder()
                        .sid(studentId)
                        .pid(department.getProfessor())
                        .years(String.valueOf(year))
                        .term(gradeService.termReturn(month))
                        .code(code)
                        .subject(department.getName())
                        .apply_credit(department.getApply_credit())
                        .credit(gradeService.creditReturn(value))
                        .grade(gradeService.gradeReturn(value))
                        .score(Float.parseFloat(value))
                        .build();
                gradeRepository.save(grade);
            }
        });
        Department department = departmentRepository.findByCode(code);
        department.setEvaluation(true);
        departmentRepository.save(department);

        return "redirect:/info/gradeList";
    }

}
