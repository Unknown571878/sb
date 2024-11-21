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
                // 기존 데이터
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
                        .build(),

                Users.builder()
                        .id("2301002")
                        .job("학생")
                        .phone("010-1224-5678")
                        .address("성남시 수정구")
                        .hireDate(LocalDate.now())
                        .birthDate(LocalDate.parse("1995-09-03"))
                        .email("kang@korea.com")
                        .department("컴퓨터소프트웨어")
                        .gender("남성")
                        .password("1234")
                        .name("홍길동2")
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
                        .build(),

                // 추가된 학생 데이터
                Users.builder()
                        .id("2301003")
                        .job("학생")
                        .phone("010-3333-5678")
                        .address("서울시 종로구")
                        .hireDate(LocalDate.now())
                        .birthDate(LocalDate.parse("1997-05-15"))
                        .email("lee@korea.com")
                        .department("인공지능")
                        .gender("여성")
                        .password("4321")
                        .name("이영희")
                        .build(),

                // 추가된 교수 데이터
                Users.builder()
                        .id("120102")
                        .job("교수")
                        .phone("010-5555-4444")
                        .address("대전광역시")
                        .hireDate(LocalDate.parse("2010-03-05"))
                        .birthDate(LocalDate.parse("1970-11-15"))
                        .email("profkim@korea.com")
                        .department("양자물리학")
                        .gender("남성")
                        .password("abcd1234")
                        .name("김유신")
                        .build()
        );
        usersRepository.saveAll(users);

        List<Department> departments = Arrays.asList(
                // 기존 데이터
                Department.builder()
                        .professor("120101")
                        .professor_name("이순신")
                        .name("인공지능")
                        .code("CS302")
                        .createAt(LocalDate.parse("2025-07-22"))
                        .apply_credit(3)
                        .time("09:00-11:50")
                        .week("화요일")
                        .apply(0)
                        .headCount(75)
                        .permission(true)
                        .build(),

                Department.builder()
                        .professor("120101")
                        .professor_name("이순신")
                        .name("양자물리학")
                        .code("PHY201")
                        .createAt(LocalDate.parse("2025-07-23"))
                        .apply_credit(3)
                        .time("09:00-11:50")
                        .week("수요일")
                        .apply(0)
                        .headCount(50)
                        .permission(true)
                        .build(),

                // 추가된 과목 데이터
                Department.builder()
                        .professor("120102")
                        .professor_name("김유신")
                        .name("딥러닝 이론")
                        .code("CS401")
                        .createAt(LocalDate.parse("2025-07-25"))
                        .apply_credit(3)
                        .time("14:00-16:50")
                        .week("목요일")
                        .apply(0)
                        .headCount(60)
                        .permission(true)
                        .build(),

                Department.builder()
                        .professor("120102")
                        .professor_name("김유신")
                        .name("고급 알고리즘")
                        .code("CS402")
                        .createAt(LocalDate.parse("2025-07-26"))
                        .apply_credit(3)
                        .time("10:00-12:50")
                        .week("금요일")
                        .apply(0)
                        .headCount(40)
                        .permission(true)
                        .build()
        );
        departmentRepository.saveAll(departments);

        List<Grade> grades = Arrays.asList(
                        Grade.builder().sid("2301001").pid("120101").years("2023").term("1학기").code("CS302").subject("인공지능").credit(3.0f).apply_credit(3).grade("A").score(95.5f).build(),
                        Grade.builder().sid("2301002").pid("120101").years("2023").term("1학기").code("PHY201").subject("양자물리학").credit(3.0f).apply_credit(3).grade("B+").score(87.0f).build(),
                        Grade.builder().sid("2301003").pid("120102").years("2023").term("1학기").code("CS401").subject("딥러닝 이론").credit(4.0f).apply_credit(3).grade("A-").score(89.0f).build(),

                        // 2023학년도 2학기
                        Grade.builder().sid("2301001").pid("120102").years("2023").term("2학기").code("CS402").subject("고급 알고리즘").credit(3.0f).apply_credit(3).grade("A+").score(98.0f).build(),
                        Grade.builder().sid("2301002").pid("120101").years("2023").term("2학기").code("CS302").subject("인공지능").credit(3.0f).apply_credit(3).grade("B").score(82.0f).build(),
                        Grade.builder().sid("2301003").pid("120102").years("2023").term("2학기").code("PHY201").subject("양자물리학").credit(3.0f).apply_credit(3).grade("A").score(91.5f).build(),
                        Grade.builder().sid("2301002").pid("120101").years("2023").term("2학기").code("PHY301").subject("양자 컴퓨팅").credit(3.0f).apply_credit(3).grade("B+").score(85.0f).build(),

                        // 2024학년도 1학기
                        Grade.builder().sid("2301001").pid("120101").years("2024").term("1학기").code("CS302").subject("인공지능").credit(3.0f).apply_credit(3).grade("A+").score(97.0f).build(),
                        Grade.builder().sid("2301002").pid("120101").years("2024").term("1학기").code("PHY201").subject("양자물리학").credit(3.0f).apply_credit(3).grade("B").score(85.0f).build(),
                        Grade.builder().sid("2301003").pid("120102").years("2024").term("1학기").code("CS402").subject("고급 알고리즘").credit(3.0f).apply_credit(3).grade("A-").score(88.0f).build(),
                        Grade.builder().sid("2301003").pid("120102").years("2024").term("1학기").code("CS401").subject("딥러닝 이론").credit(4.0f).apply_credit(3).grade("A").score(92.0f).build()
                );

        gradeRepository.saveAll(grades);


        IntStream.range(0, 300).forEach(i -> {
            Post postList = Post.builder()
                    .title("제목 "+i)
                    .createdAt(LocalDateTime.now())
                    .content("내용" + i)
                    .cnt(0)
                    .name("admin")
                    .type("free")
                    .userid(0L)
                    .build();
            postRepository.save(postList);
        });

        List<Post> noticeList = Arrays.asList(
                Post.builder()
                        .title("2024년 1학기 수강신청 안내")
                        .createdAt(LocalDateTime.now().minusDays(10))
                        .content("2024년 1학기 수강신청이 시작됩니다. 자세한 사항은 홈페이지를 참고하세요.")
                        .cnt(0)
                        .name("학사관리팀")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("졸업식 일정 안내")
                        .createdAt(LocalDateTime.now().minusDays(8))
                        .content("2024년도 졸업식은 2월 25일(일)에 진행됩니다. 졸업생 여러분의 많은 참여 바랍니다.")
                        .cnt(0)
                        .name("교무처")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("동계 방학기간 도서관 운영 시간 안내")
                        .createdAt(LocalDateTime.now().minusDays(6))
                        .content("동계 방학기간 동안 도서관 운영시간이 단축됩니다. 평일 09:00~18:00, 주말 휴관.")
                        .cnt(0)
                        .name("도서관")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("2024학년도 신입생 오리엔테이션 안내")
                        .createdAt(LocalDateTime.now().minusDays(5))
                        .content("2024학년도 신입생 오리엔테이션은 3월 1일 진행됩니다. 자세한 내용은 이메일로 발송 예정.")
                        .cnt(0)
                        .name("학생처")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("등록금 납부 안내")
                        .createdAt(LocalDateTime.now().minusDays(3))
                        .content("2024학년도 1학기 등록금 납부 기간은 2월 10일부터 2월 20일까지입니다.")
                        .cnt(0)
                        .name("재무팀")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("겨울 계절학기 수강 안내")
                        .createdAt(LocalDateTime.now().minusDays(15))
                        .content("겨울 계절학기 수강신청이 시작되었습니다. 마감일: 12월 15일.")
                        .cnt(0)
                        .name("학사관리팀")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("장학금 신청 안내")
                        .createdAt(LocalDateTime.now().minusDays(20))
                        .content("2024학년도 1학기 장학금 신청 기간: 12월 1일부터 12월 31일까지.")
                        .cnt(0)
                        .name("학생처")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("캠퍼스 내 정전 안내")
                        .createdAt(LocalDateTime.now().minusDays(12))
                        .content("12월 20일 오전 9시부터 12시까지 정기 점검으로 인한 정전이 예정되어 있습니다.")
                        .cnt(0)
                        .name("시설관리팀")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("교내 동아리 모집 공고")
                        .createdAt(LocalDateTime.now().minusDays(18))
                        .content("2024년도 교내 동아리 회원 모집이 시작됩니다. 관심 있는 학생들은 동아리 회장에게 문의하세요.")
                        .cnt(0)
                        .name("학생처")
                        .type("notice")
                        .userid(1L)
                        .build(),

                Post.builder()
                        .title("기말고사 일정 공지")
                        .createdAt(LocalDateTime.now().minusDays(25))
                        .content("2023년도 2학기 기말고사가 12월 10일부터 12월 16일까지 진행됩니다.")
                        .cnt(0)
                        .name("교무처")
                        .type("notice")
                        .userid(1L)
                        .build()
        );
        postRepository.saveAll(noticeList);
    }
}
