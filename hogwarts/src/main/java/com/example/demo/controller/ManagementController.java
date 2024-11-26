package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Users;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.ScheduleRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/management")
public class ManagementController {

    private final ScheduleRepository scheduleRepository;
    private final DepartmentRepository departmentRepository;
    private final UsersRepository usersRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/main")
    public String mainManagement() {
        return "/management/main";
    }

    @GetMapping("/schedule")
    public String schedule(@RequestParam(required = false) Integer year,
                           Model model) {
        // 현재 연도와 월 기본값 설정
        LocalDate currentDate = LocalDate.now();
        int currentYear = (year != null) ? year : currentDate.getYear();
        Year currentYearYear = Year.of(currentYear);

        YearMonth yearMonth1 = YearMonth.of(currentYear, 1);
        YearMonth yearMonth12 = YearMonth.of(currentYear, 12);
        LocalDate startOfMonth = yearMonth1.atDay(1);
        LocalDate endOfMonth = yearMonth12.atEndOfMonth();

        int prevYear = currentYear - 1;
        int nextYear = currentYear + 1;

        List<Schedule> schedules = scheduleRepository.findByScheduleDateBetweenOrderByScheduleDate(startOfMonth, endOfMonth);
        model.addAttribute("schedules", schedules);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("prevYear", prevYear);
        model.addAttribute("nextYear", nextYear);


        return "/management/schedule"; // HTML 템플릿 경로
    }
    @PostMapping("/scheduleInsert")
    public String scheduleInsert(@ModelAttribute("inputDate") String inputDate,
                                 @ModelAttribute("title") String title, Model model) {
        Schedule schedule = Schedule.builder().title(title).scheduleDate(LocalDate.parse(inputDate)).build();
        scheduleRepository.save(schedule);

        MessageDto message = new MessageDto("일정이 추가되었습니다", "/management/schedule", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
    @GetMapping("/scheduleDelete")
    public String scheduleDelete(@RequestParam("id")Long id, Model model) {
        scheduleRepository.deleteById(id);
        MessageDto message = new MessageDto("일정을 삭제하였습니다", "/management/schedule", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
    @GetMapping("/department")
    public String department(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "/management/department";
    }
    @GetMapping("/departmentPermssion")
    public String departmentPermssion(@RequestParam("id")Long id, Model model) {
        Department department = departmentRepository.findById(id).orElse(null);
        department.setPermission(true);
        departmentRepository.save(department);
        MessageDto message = new MessageDto("승인하였습니다", "/management/department", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
    @PostMapping("/users/add")
    public String userInsert(@ModelAttribute Users user, Model model) {
        log.info(user.toString());
        usersRepository.save(user);
        MessageDto message = new MessageDto("사용자를 추가하였습니다", "/management/main", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
}
