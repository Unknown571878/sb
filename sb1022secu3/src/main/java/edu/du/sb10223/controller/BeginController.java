package edu.du.sb10223.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeginController {

    @GetMapping("/")
    public String index() {
        return "sample/all";
    }
}
