package edu.du.maptest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {
    @GetMapping("/")
    public String showMap() {
        return "/index"; // templates/map.html 파일 렌더링
    }

    @GetMapping("kakaoMap")
    public String showKakaoMap() {
        return "/kakaoMap";
    }
}
