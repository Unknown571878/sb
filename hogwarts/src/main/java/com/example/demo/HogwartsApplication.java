package com.example.demo;

import com.example.demo.entity.Department;
import com.example.demo.entity.Grade;
import com.example.demo.entity.Post;
import com.example.demo.entity.Users;
import com.example.demo.reopository.DepartmentRepository;
import com.example.demo.reopository.GradeRepository;
import com.example.demo.reopository.PostRepository;
import com.example.demo.reopository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class HogwartsApplication {

    private final GradeRepository gradeRepository;

    private final UsersRepository usersRepository;

    private final PostRepository postRepository;

    private final DepartmentRepository departmentRepository;

    public static void main(String[] args) {
        SpringApplication.run(HogwartsApplication.class, args);
    }

    @PostConstruct
    public void init(){
        List<Users> users = Arrays.asList(
                Users.builder()
                        .id("admin")
                        .job("관리자")
                        .phone("010-1111-1111")
                        .address("서울")
                        .email("admin@hogwarts.com")
                        .password("123456")
                        .build(),

                Users.builder()
                        .id("2301001")
                        .job("학생")
                        .phone("010-1234-5678")
                        .address("성남시 수정구")
                        .hireDate(LocalDate.now())
                        .birthDate(LocalDate.parse("1995-09-03"))
                        .email("hong@korea.com")
                        .department("컴퓨터소프트웨어")
                        .gender("남성")
                        .password("1234")
                        .name("홍길동")
                        .gId(1L)
                        .build(),

                Users.builder()
                        .id("120101")
                        .job("교수")
                        .phone("010-1111-5555")
                        .address("경기도 고양시")
                        .hireDate(LocalDate.parse("2012-01-12"))
                        .birthDate(LocalDate.parse("1968-02-22"))
                        .email("professor@korea.com")
                        .department("컴퓨터소프트웨어")
                        .gender("남성")
                        .password("1234")
                        .name("이순신")
                        .build()
        );
        usersRepository.saveAll(users);

        Department department = Department.builder()
                .professor("120101")
                .professor_name("이순신")
                .name("인공지능")
                .code("CS302")
                .createAt(LocalDate.parse("2025-07-22"))
                .time("09:00-11:50")
                .week("화요일")
                .apply(0)
                .headCount(75)
                .permission(true)
                .build();
        departmentRepository.save(department);

        List<Grade> grades = Arrays.asList(
                // 2023년 1학기
                Grade.builder().sid("2301001").pid("120101").years("2023").term("1학기").code("CS101").subject("자료구조").credit(3).grade("A+").score(95).build(),
                Grade.builder().sid("2301001").pid("120101").years("2023").term("1학기").code("MATH101").subject("선형대수").credit(3).grade("A").score(90).build(),
                Grade.builder().sid("2301001").pid("120101").years("2023").term("1학기").code("ENG101").subject("영어회화").credit(2).grade("B+").score(88).build(),

                // 2023년 2학기
                Grade.builder().sid("2301001").pid("120101").years("2023").term("2학기").code("CS102").subject("알고리즘").credit(3).grade("A").score(92).build(),
                Grade.builder().sid("2301001").pid("120101").years("2023").term("2학기").code("PHY101").subject("물리학").credit(4).grade("B").score(85).build(),

                // 2024년 1학기
                Grade.builder().sid("2301001").pid("120101").years("2024").term("1학기").code("CS201").subject("데이터베이스").credit(3).grade("A+").score(97).build(),
                Grade.builder().sid("2301001").pid("120101").years("2024").term("1학기").code("MATH201").subject("미적분학").credit(3).grade("B+").score(87).build(),
                Grade.builder().sid("2301001").pid("120101").years("2024").term("1학기").code("HIST101").subject("세계사").credit(2).grade("A").score(90).build(),

                // 2024년 2학기
                Grade.builder().sid("2301001").pid("120101").years("2024").term("2학기").code("CS202").subject("컴퓨터 네트워크").credit(3).grade("B").score(82).build(),
                Grade.builder().sid("2301001").pid("120101").years("2024").term("2학기").code("CHEM101").subject("화학").credit(4).grade("A").score(91).build(),
                Grade.builder().sid("2301001").pid("120101").years("2024").term("2학기").code("PHY301").subject("물리학").credit(4).grade("A").score(90).build(),

                // 2025년 1학기
                Grade.builder().sid("2301001").pid("120101").years("2025").term("1학기").code("BIO101").subject("생물학").credit(3).grade("A+").score(96).build(),
                Grade.builder().sid("2301001").pid("120101").years("2025").term("1학기").code("CS301").subject("운영체제").credit(3).grade("A").score(94).build(),
                Grade.builder().sid("2301001").pid("120101").years("2025").term("1학기").code("MATH301").subject("확률과 통계").credit(3).grade("B+").score(88).build(),

                // 2025년 2학기
                Grade.builder().sid("2301001").pid("120101").years("2025").term("2학기").code("CS302").subject("인공지능").credit(3).grade("A+").score(98).build(),
                Grade.builder().sid("2301001").pid("120101").years("2025").term("2학기").code("PHY201").subject("양자물리학").credit(4).grade("A").score(93).build(),
                Grade.builder().sid("2301001").pid("120101").years("2025").term("2학기").code("ENG201").subject("고급영어").credit(2).grade("B").score(84).build()
        );
        // 데이터 저장
        gradeRepository.saveAll(grades);

        IntStream.range(0, 300).forEach(i -> {
            Post postList = Post.builder()
                    .title("제목 "+i)
                    .createdAt(LocalDateTime.now())
                    .content("내용" + i)
                    .cnt(0)
                    .name("admin")
                    .userid(0L)
                    .build();
            postRepository.save(postList);
        });
    }
}
