package com.example.demo.repository;

import com.example.demo.entity.Inquiry;
import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    public List<Inquiry> findAllByOrderByPidDesc();
    public List<Inquiry> findByTypeAndUidOrderByPidDesc(String type, String uid);
    public List<Inquiry> findByPidAndTypeOrderByCreateAtDesc(Long pid, String type);
}
