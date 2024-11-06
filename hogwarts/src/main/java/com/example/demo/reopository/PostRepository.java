package com.example.demo.reopository;

import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findAllByOrderByPidDesc();
}
