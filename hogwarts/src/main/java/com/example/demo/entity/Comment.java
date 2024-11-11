package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    private String name; // 댓글 작성자 이름

    private String content; // 댓글 내용

    // 댓글이 속한 게시글과의 관계
    @ManyToOne
    @JoinColumn(name = "post_id") // 외래 키로 post_id 사용
    private Post post; // 해당 댓글이 속한 게시글
}
