package edu.du.myproject;

import edu.du.myproject.entity.PostList;
import edu.du.myproject.repository.PostListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class MyprojectApplication {

    private final PostListRepository postListRepository;

    public static void main(String[] args) {
        SpringApplication.run(MyprojectApplication.class, args);
    }

    @PostConstruct
    public void init() {
        IntStream.range(0, 300).forEach(i -> {
            PostList postList = PostList.builder()
                    .title("제목 "+i)
                    .createdAt(LocalDateTime.now())
                    .content("내용" + i)
                    .cnt(0)
                    .name("admin")
                    .build();
            postListRepository.save(postList);
        });
    }
}
