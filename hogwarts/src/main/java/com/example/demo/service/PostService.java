package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.reopository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

    // 게시글 조회
    public Post getPostById(Long pid) {
        return postRepository.findById(pid).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
    }

    // 댓글 추가
    public void addCommentToPost(Long pid, String content) {
        Post post = getPostById(pid);
        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setName("Anonymous"); // 임시로 작성자 이름을 "Anonymous"로 설정
        post.getComments().add(newComment);
        postRepository.save(post); // 댓글을 추가한 게시글 저장
    }

    // 게시글 삭제
    public void deletePost(Long pid) {
        postRepository.deleteById(pid);
    }
}
