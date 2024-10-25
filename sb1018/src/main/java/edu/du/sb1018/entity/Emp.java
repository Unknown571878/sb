package edu.du.sb1018.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Setter
@Getter
@ToString
@Entity
public class Emp {
    @Id
    private Integer empno;
    private String ename;
    private String job;
    private String mgr;
    private Date hiredate;
    private Double sal;
    private Double comm;
    private Integer deptno;
}
