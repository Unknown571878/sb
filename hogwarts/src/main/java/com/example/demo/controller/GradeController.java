package com.example.demo.controller;

import com.example.demo.entity.Grade;
import com.example.demo.reopository.GradeRepository;
import com.example.demo.service.AuthInfo;
import com.example.demo.service.GradeService;
import com.example.demo.service.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeRepository gradeRepository;
    private final GradeService gradeService;

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
                List<Grade> grades = gradeService.getGradesByYearAndTerm(year, term);
                List<Map<String, Object>> stats = gradeService.calculateStatistics();

                model.addAttribute("grades", grades);
                model.addAttribute("yearTermStats", stats);
            }
            Map<String, Double> overallStats = gradeService.calculateOverallStatistics();
            model.addAttribute("overallStats", overallStats);
        } else {
            MessageDto message = new MessageDto("로그인이 필요한 서비스입니다", "/loginForm", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        return "/info/grade";
    }

}
