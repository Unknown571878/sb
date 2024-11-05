package com.example.demo;

import com.example.demo.entity.Grade;
import com.example.demo.entity.Users;
import com.example.demo.reopository.GradeRepository;
import com.example.demo.reopository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
public class HogwartsApplication {

    private final GradeRepository gradeRepository;

    private final UsersRepository usersRepository;

    public static void main(String[] args) {
        SpringApplication.run(HogwartsApplication.class, args);
    }

    @PostConstruct
    public void init(){
        Users users = Users.builder()
                .id("230001")
                .job("Student")
                .phone("010-1234-5678")
                .address("성남시 수정구")
                .hireDate(LocalDate.now())
                .birthDate(LocalDate.parse("1995-09-03"))
                .email("hong@korea.com")
                .gender("남성")
                .password("1234")
                .name("홍길동")
                .gId(1L)
                .build();
        Grade grade = Grade.builder()
                .kor(85.7F)
                .math(80.2f)
                .eng(92.1f)
                .total(258)
                .avg(86)
                .build();

        gradeRepository.save(grade);
        usersRepository.save(users);
    }
}
