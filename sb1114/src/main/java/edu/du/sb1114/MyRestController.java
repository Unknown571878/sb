package edu.du.sb1114;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
public class MyRestController {
    @GetMapping("/hello")
    public LocalDateTime hello() {
        return LocalDateTime.now();
    }

    @GetMapping("/Account/{name}")
    public String Account(@PathVariable String name) {
        log.info(name);
        return name;
    }

//    @PostMapping("/account")
//    public String Account(@RequestBody Account account) {
//        log.info(account.toString());
//        return account.toString();
//    }
    @PostMapping("/account")
    public ResponseEntity<?> account(@RequestBody Account account) {
        log.info(account.toString());
        return ResponseEntity.ok(account);
    }
}
