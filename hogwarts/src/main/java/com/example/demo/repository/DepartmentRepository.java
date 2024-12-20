package com.example.demo.repository;

import com.example.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    public List<Department> findByProfessorAndPermission(String professor, boolean permission);
    public List<Department> findByProfessorOrderByIdDesc(String professor);
    public Department findByCode(String code);
}
