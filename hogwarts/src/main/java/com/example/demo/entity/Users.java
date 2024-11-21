package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uId;

    private String id;

    private String name;

    private String email;

    private String password;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate hireDate;

    private LocalDate birthDate;

    private String address;

    private String phone;

    private String gender;

    private String job;

    private String department;

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
