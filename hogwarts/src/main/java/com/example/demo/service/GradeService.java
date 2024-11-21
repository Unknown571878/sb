package com.example.demo.service;

import com.example.demo.entity.Grade;
import com.example.demo.reopository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<Grade> getGradesByYearAndTerm(String year, String term, String sid) {
        return gradeRepository.findByYearsAndTermAndSid(year, term, sid);
    }

    public List<String> getAllYears(String sid) {
        return gradeRepository.findBySid(sid).stream()
                .map(Grade::getYears)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getAllTerms(String sid) {
        return gradeRepository.findBySid(sid).stream()
                .map(Grade::getTerm)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> calculateStatistics(String sid) {
        List<Grade> grades = gradeRepository.findBySid(sid);

        // 년도-학기별로 그룹화
        Map<String, List<Grade>> groupedByYearTerm = grades.stream()
                .collect(Collectors.groupingBy(grade -> grade.getYears() + "-" + grade.getTerm()));

        // 통계 계산
        List<Map<String, Object>> stats = new ArrayList<>();
        for (Map.Entry<String, List<Grade>> entry : groupedByYearTerm.entrySet()) {
            String[] yearTerm = entry.getKey().split("-");
            String year = yearTerm[0];
            String term = yearTerm[1];
            List<Grade> semesterGrades = entry.getValue();

            double totalCredits = semesterGrades.stream().mapToDouble(Grade::getCredit).sum();
            double total_apply_Credits = semesterGrades.stream().mapToDouble(Grade::getApply_credit).sum();
            double totalScore = semesterGrades.stream().mapToDouble(Grade::getScore).sum();
            double avgCredit = roundToThreeDecimalPlaces(totalCredits / semesterGrades.size());
            double avgScore = roundToThreeDecimalPlaces(totalScore / semesterGrades.size());

            Map<String, Object> stat = new HashMap<>();
            stat.put("year", year);
            stat.put("term", term);
            stat.put("avgCredit", avgCredit);
            stat.put("avgScore", avgScore);
            stat.put("totalCredits", total_apply_Credits);

            stats.add(stat);
        }

        // 년도와 학기를 기준으로 정렬
        stats.sort((stat1, stat2) -> {
            int yearComparison = ((String) stat1.get("year")).compareTo((String) stat2.get("year"));
            if (yearComparison != 0) {
                return yearComparison;
            }
            // 학기 정렬: "1학기"가 "2학기"보다 먼저 오도록
            return ((String) stat1.get("term")).compareTo((String) stat2.get("term"));
        });

        return stats;
    }


    public Map<String, Double> calculateOverallStatistics(String sid) {
        List<Grade> grades = gradeRepository.findBySid(sid);

        double totalCredits = grades.stream().mapToDouble(Grade::getCredit).sum();
        double total_apply_Credits = grades.stream().mapToDouble(Grade::getApply_credit).sum();
        double totalScore = grades.stream().mapToDouble(Grade::getScore).sum();
        double avgCredit = roundToThreeDecimalPlaces(totalCredits / grades.size());
        double avgScore = roundToThreeDecimalPlaces(totalScore / grades.size());

        Map<String, Double> overallStats = new HashMap<>();
        overallStats.put("avgCredit", avgCredit);
        overallStats.put("avgScore", avgScore);
        overallStats.put("totalCredits", total_apply_Credits);

        return overallStats;
    }

    private double roundToThreeDecimalPlaces(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

    public String gradeReturn(String score){
        String grade = "";
        float myScore = Float.parseFloat(score);
        if(myScore >= 95){
            grade = "A+";
        } else if(myScore >= 90 && myScore <= 94){
            grade = "A";
        } else if(myScore > 84 && myScore < 90){
            grade = "B+";
        } else if(myScore > 79 && myScore < 85){
            grade = "B";
        } else if(myScore > 74 && myScore < 80){
            grade = "C+";
        } else if(myScore > 69 && myScore < 75){
            grade = "C";
        } else if(myScore > 64 && myScore < 70){
            grade = "D+";
        } else if(myScore > 59 && myScore < 65){
            grade = "D";
        } else {
            grade = "F";
        }
        return grade;
    }

    public float creditReturn(String score){
        float credit = 0;
        float myScore = Float.parseFloat(score);
        if(myScore >= 95){
            credit = 4.5f;
        } else if(myScore >= 90 && myScore <= 94){
            credit = 4.0f;
        } else if(myScore > 84 && myScore < 90){
            credit = 3.5f;
        } else if(myScore > 79 && myScore < 85){
            credit = 3.0f;
        } else if(myScore > 74 && myScore < 80){
            credit = 2.5f;
        } else if(myScore > 69 && myScore < 75){
            credit = 2.0f;
        } else if(myScore > 64 && myScore < 70){
            credit = 1.5f;
        } else if(myScore > 59 && myScore < 65){
            credit = 1.0f;
        } else {
            credit = 0.0f;
        }
        return credit;
    }

    public String termReturn(int month){
        if (month <= 6)
            return "1학기";
        else
            return "2학기";
    }

}
