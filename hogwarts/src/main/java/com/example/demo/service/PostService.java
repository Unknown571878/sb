package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 조회
    public Post getPostById(Long pid) {
        return postRepository.findById(pid).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
    }

    // 댓글 추가
    public void addCommentToPost(Long pid,String id, String name, String content, Long uid) {
        Post post = getPostById(pid);
        Comment newComment = new Comment();
        newComment.setUserId(uid);
        newComment.setContent(content);
        newComment.setName(name);
        newComment.setAuthorId(id);
        newComment.setCreatedAt(LocalDateTime.now());

        // 댓글이 속한 게시글 설정
        newComment.setPost(post); // 여기서 post를 명시적으로 설정해줍니다.

        // 댓글을 게시글의 댓글 리스트에 추가
        post.getComments().add(newComment);

        // 게시글과 댓글을 함께 저장 (cascade 설정이므로 댓글도 함께 저장됩니다.)
        postRepository.save(post); // 댓글을 추가한 게시글 저장
    }

    // 게시글 삭제
    public void deletePost(Long pid) {
        postRepository.deleteById(pid);
    }
}
