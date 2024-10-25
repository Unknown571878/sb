//package edu.du.sb1018.controller;
//
//import edu.du.sb1018.entity.Dept;
//import edu.du.sb1018.entity.Emp;
//import edu.du.sb1018.service.EmService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//public class TestController {
//
////    @Autowired
//    final EmService emService;
//
//    @GetMapping("/")
//    public List<Dept> index() {
//        return emService.getAllDept();
//    }
//
//    @GetMapping("/{deptno}")
//    public List<Emp> getemp(@PathVariable int deptno) {
//        return emService.getEmp(deptno);
//    }
//
//    @GetMapping("/{deptno}/{dname}")
//    public Dept index(@PathVariable int deptno, @PathVariable String dname) {
//        return emService.updateDept(deptno,dname);
//    }
//}
