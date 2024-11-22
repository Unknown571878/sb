package com.example.demo.repository;

import com.example.demo.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    public List<Grade> findBySid(String id);
    public List<Grade> findByYearsAndTermAndSid(String year, String term, String sid);
}
