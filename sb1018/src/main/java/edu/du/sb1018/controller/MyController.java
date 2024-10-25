package edu.du.sb1018.controller;

import edu.du.sb1018.entity.Dept;
import edu.du.sb1018.entity.Emp;
import edu.du.sb1018.service.EmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MyController {

    final EmService emService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("depts", emService.getAllDept());
        return "index";
    }

    @GetMapping("/emp")
    public String emp(Model model, int deptNo) {
        model.addAttribute("emps", emService.getEmp(deptNo));
        return "emp";
    }

    @GetMapping("/insertDept")
    public String insertDept() {
        return "insertDept";
    }

    @PostMapping("/Dinsert")
    public String dinsert(Dept dept) {
        emService.insertDept(dept);
        return "redirect:/";
    }

    @GetMapping("/insertEmp")
    public String insertEmp() {
        return "insertEmp";
    }

    @PostMapping("/Einsert")
    public String einsert(Emp emp) {
        emService.insertEmp(emp);
        return "redirect:/";
    }

    @GetMapping("/ddelete")
    public String ddelete(int deptNo) {
        emService.deleteDept(deptNo);
        return "redirect:/";
    }

    @GetMapping("edelete")
    public String edelete(int empNo) {
        emService.deleteEmp(empNo);
        return "redirect:/";
    }

    @GetMapping("/dupdate")
    public String updateDept(Model model, int deptNo) {
        model.addAttribute("deptno", deptNo);
        return "updateDept";
    }

    @PostMapping("updateDept")
    public String updateDept(Dept dept) {
        emService.updateDept(dept);

        return "redirect:/";
    }

    @GetMapping("/updateEmp")
    public String updateEmp(Model model, int empNo) {
        model.addAttribute("empno", empNo);
        return "updateEmp";
    }

    @PostMapping("/Eupdate")
    public String eupdate(Emp emp) {
        emService.updateEmp(emp);

        return "redirect:/";
    }
}
