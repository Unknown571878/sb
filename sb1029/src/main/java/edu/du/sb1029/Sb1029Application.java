package edu.du.sb1029;

import edu.du.sb1029.entity.Notice;
import edu.du.sb1029.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class Sb1029Application {

    final NoticeRepository noticeRepository;

    public static void main(String[] args) {
        SpringApplication.run(Sb1029Application.class, args);
    }

    @PostConstruct
    public void init() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Notice notice = Notice.builder()
                    .title("제목" + i)
                    .createdAt(LocalDateTime.now())
                    .content("내용" + i)
                    .hitCnt(0)
                    .build();
            noticeRepository.save(notice);
        });
    }
}
