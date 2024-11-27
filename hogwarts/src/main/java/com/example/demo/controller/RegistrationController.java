package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Registration;
import com.example.demo.entity.Users;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RegistrationRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.AuthInfo;
import com.example.demo.service.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/registration")
public class RegistrationController {

    private final DepartmentRepository departmentRepository;
    private final RegistrationRepository registrationRepository;
    private final UsersRepository usersRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/registration-p")
    public String registration_pPage(Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        List<Department> departments = departmentRepository.findByProfessorAndPermission(authInfo.getId(), true);
        model.addAttribute("departments", departments);
        return "/registration/registration-p";
    }

    @GetMapping("/registration-s")
    public String registration_sPage(HttpSession session, Model model) {
        if(session.getAttribute("authInfo") == null) {
            MessageDto message = new MessageDto("로그인이 필요한 서비스입니다", "/loginForm", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        List<Registration> registrations = registrationRepository.findAll();
        List<Department> departments = new ArrayList<>();
        for (Registration registration : registrations) {
            if (authInfo.getId().equals(registration.getSid())) {
                departments.add(departmentRepository.findByCode(registration.getCode()));
            }
        }
        model.addAttribute("departments", departments);
        return "/registration/registration-s";
    }

    @GetMapping("/application-s")
    public String application_sPage(Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        List<Department> departments = departmentRepository.findAll();
        List<Registration> registrations = registrationRepository.findAll();

        // 사용자 등록 정보를 기반으로 삭제할 코드 리스트를 생성
        List<String> registeredCodes = new ArrayList<>();
        for (Registration registration : registrations) {
            if (authInfo.getId().equals(registration.getSid())) {
                registeredCodes.add(registration.getCode());
            }
        }
        // departments에서 등록된 코드와 일치하는 Department 제거
        departments.removeIf(department -> registeredCodes.contains(department.getCode()));

        model.addAttribute("departments", departments);
        return "/registration/application-s";
    }

    @GetMapping("/application")
    public String applicationPage(@RequestParam("id") Long id, Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        Department department = departmentRepository.findById(id).orElse(null);
        if (department.getApply() + 1 > department.getHeadCount()) {
            MessageDto message = new MessageDto("해당 강좌는 더 이상 신청할 수 없습니다.", "/registration/registration-s", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        } else {
            Registration registration = Registration.builder()
                    .code(department.getCode())
                    .sid(authInfo.getId())
                    .pid(department.getProfessor())
                    .build();
            department.setApply(department.getApply() + 1);
            departmentRepository.save(department);
            registrationRepository.save(registration);
            MessageDto message = new MessageDto("신청되었습니다.", "/registration/registration-s", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }
    }

    @GetMapping("/application-p")
    public String application_pPage(Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        List<Department> departments = departmentRepository.findByProfessorOrderByIdDesc(authInfo.getId());
        model.addAttribute("departments", departments);
        return "/registration/application-p";
    }

    @GetMapping("/createDepartment")
    public String createDepartmentPage(Model model, HttpSession session) {
        return "/registration/createDepartment";
    }
    @GetMapping("/attendanceRegister-p")
    public String attendanceRegisterPage(@RequestParam("code") String code, Model model, HttpSession session) {
        List<Registration> registrations = registrationRepository.findByCode(code);
        Department department = departmentRepository.findByCode(code);
        if (department.getCount() >= 16){
            MessageDto message = new MessageDto("수업을 모두 진행하였습니다", "/registration/registration-p", RequestMethod.GET, null);
            return showMessageAndRedirect(message, model);
        }

        List<Users> users = new ArrayList<>();

        for (Registration registration : registrations) {
            Users user = usersRepository.findById(registration.getSid());
            users.add(user);
        }
        model.addAttribute("users", users);
        model.addAttribute("code", code);
        return "/registration/attendanceRegister-p";
    }

    @PostMapping("/submit-attendance")
    public String submitAttendance(@RequestParam Map<String, String> attendances,
                                   @RequestParam String code, Model model) {
        Department department = departmentRepository.findByCode(code);
        int time = department.getCount();
        department.setCount(time + 1);
        departmentRepository.save(department);
        attendances.forEach((key, value) -> {
                    if (key.startsWith("attendance-")) {
                        String id = key.substring(11);
                        Registration registration = registrationRepository.findBySidAndCode(id, code);
                        if (value.equals("출석")){
                            int count = registration.getAttendance();
                            registration.setAttendance(count + 1);
                            registrationRepository.save(registration);
                        } else if (value.equals("지각")) {
                            int count = registration.getPerception();
                            registration.setPerception(count + 1);
                            registrationRepository.save(registration);
                        } else if (value.equals("조퇴")) {
                            int count = registration.getEarly_leave();
                            registration.setEarly_leave(count + 1);
                            registrationRepository.save(registration);
                        } else if (value.equals("결석")) {
                            int count = registration.getAbsence();
                            registration.setAbsence(count + 1);
                            registrationRepository.save(registration);
                        }
                    }
                });

        MessageDto message = new MessageDto("입력하였습니다.", "/registration/registration-p", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @PostMapping("/submitApplication")
    public String submitApplication(@ModelAttribute("department") Department department, HttpSession session, Model model,
                                    @RequestParam("start-time") String start_time,
                                    @RequestParam("end-time") String end_time) {
        String time = start_time + "-" + end_time;
        Department newDepartment = Department.builder()
                .professor(department.getProfessor())
                .professor_name(department.getProfessor_name())
                .name(department.getName())
                .code(department.getCode())
                .createAt(LocalDate.now())
                .time(time)
                .week(department.getWeek())
                .apply_credit(department.getApply_credit())
                .apply(department.getApply())
                .headCount(department.getHeadCount())
                .evaluation(false)
                .permission(false)
                .build();
        departmentRepository.save(newDepartment);
        log.info(newDepartment.toString());
        MessageDto message = new MessageDto("신청되었습니다.", "/registration/application-p", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    @GetMapping("/attendance")
    public String attendancePage(@RequestParam("code") String code, Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        Registration registration = registrationRepository.findBySidAndCode(authInfo.getId(), code);
        Department department = departmentRepository.findByCode(code);
        model.addAttribute("department", department);
        model.addAttribute("registration", registration);
        return "/registration/attendance";
    }
}
