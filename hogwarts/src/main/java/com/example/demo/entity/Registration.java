package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code; // 신청 과목 번호
    private String pid; // 교번
    private String sid; // 학번

    @Builder.Default
    private int attendance=0; //출석
    @Builder.Default
    private int absence=0; //결석
    @Builder.Default
    private int perception=0; //지각
    @Builder.Default
    private int early_leave=0; //조퇴
}
