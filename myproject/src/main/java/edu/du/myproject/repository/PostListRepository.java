package edu.du.myproject.repository;

import edu.du.myproject.entity.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostListRepository extends JpaRepository<PostList, Long> {
    public List<PostList> findAllByOrderByPidDesc();
}
