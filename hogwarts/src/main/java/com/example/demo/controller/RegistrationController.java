package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Registration;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RegistrationRepository;
import com.example.demo.service.AuthInfo;
import com.example.demo.service.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/registration")
public class RegistrationController {

    private final DepartmentRepository departmentRepository;
    private final RegistrationRepository registrationRepository;

    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "/common/messageRedirect";
    }

    @GetMapping("/registration-p")
    public String registration_pPage(Model model, HttpSession session) {
        AuthInfo authInfo = (AuthInfo)session.getAttribute("authInfo");
        List<Department> departments = departmentRepository.findByProfessor(authInfo.getId());
        model.addAttribute("departments", departments);
        return "/registration/registration-p";
    }
    @GetMapping("/registration-s")
    public String registration_sPage(HttpSession session, Model model) {
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
}
