package edu.du.sb1018.service;

import edu.du.sb1018.entity.Dept;
import edu.du.sb1018.entity.Emp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.List;

@Service
@Slf4j
public class EmService {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;

    public Dept updateDept(Dept newDept) {
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Dept dept = em.find(Dept.class, newDept.getDeptno());  // find = select
        if(dept != null) {
            dept.setDname(newDept.getDname());
            dept.setLoc(newDept.getLoc());
            log.info("update dept {} with name {}",newDept.getDeptno(), newDept.getDname());
        } else {
            log.info("해당 {} 부서가 없습니다.", newDept.getDeptno());
        }
        System.out.println(dept);
        transaction.commit();

        return dept;
    }

    public Emp updateEmp(Emp newEmp) {
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Emp emp = em.find(Emp.class, newEmp.getEmpno());  // find = select
        if(emp != null) {
            emp.setComm(newEmp.getComm());
            emp.setJob(newEmp.getJob());
            emp.setDeptno(newEmp.getDeptno());
            emp.setEname(newEmp.getEname());
            emp.setMgr(newEmp.getMgr());
            emp.setSal(newEmp.getSal());
            emp.setHiredate(newEmp.getHiredate());
            emp.setEname(newEmp.getEname());
        }
        System.out.println(emp);
        transaction.commit();

        return emp;
    }

    public List<Dept> getAllDept() {
        TypedQuery<Dept> query = em.createQuery("select d from Dept d", Dept.class);
        List<Dept> depts = query.getResultList();

        return depts;
    }

    public List<Emp> getEmp(int deptNo) {
        String jpql = "select d from Emp d where d.deptno = :deptNo";
        TypedQuery<Emp> query = em.createQuery(jpql, Emp.class);
        query.setParameter("deptNo", deptNo);
        List<Emp> emps = query.getResultList();
        return emps;
    }

    public void deleteEmp(int empNo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Emp emp = em.find(Emp.class, empNo);
        em.remove(emp);
        transaction.commit();
    }

    public void deleteDept(int deptNo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Dept dept = em.find(Dept.class, deptNo);
        em.remove(dept);
        transaction.commit();
    }

    public void insertDept(Dept newDept) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        em.persist(newDept);
        em.getTransaction().commit();
    }

    public void insertEmp(Emp newEmp) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(newEmp);
        em.getTransaction().commit();
    }
}
