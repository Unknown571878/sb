package edu.du.sb1022secu4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample/")
public class SampleController {

    @GetMapping("/accessDenied")
    public void accessDenied() {}

    @GetMapping("/admin")
    public void admin() {}

    @GetMapping("/member")
    public void member() {}

    @GetMapping("/all")
    public void all() {}
}
