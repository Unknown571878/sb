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

    public List<Grade> getGradesByYearAndTerm(String year, String term) {
        return gradeRepository.findByYearsAndTerm(year, term);
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

    public List<Map<String, Object>> calculateStatistics() {
        List<Grade> grades = gradeRepository.findAll();

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

            double totalCredits = semesterGrades.stream().mapToInt(Grade::getCredit).sum();
            double totalScore = semesterGrades.stream().mapToDouble(Grade::getScore).sum();
            double avgCredit = roundToThreeDecimalPlaces(totalCredits / semesterGrades.size());
            double avgScore = roundToThreeDecimalPlaces(totalScore / semesterGrades.size());

            Map<String, Object> stat = new HashMap<>();
            stat.put("year", year);
            stat.put("term", term);
            stat.put("avgCredit", avgCredit);
            stat.put("avgScore", avgScore);
            stat.put("totalCredits", totalCredits);

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


    public Map<String, Double> calculateOverallStatistics() {
        List<Grade> grades = gradeRepository.findAll();

        double totalCredits = grades.stream().mapToInt(Grade::getCredit).sum();
        double totalScore = grades.stream().mapToDouble(Grade::getScore).sum();
        double avgCredit = roundToThreeDecimalPlaces(totalCredits / grades.size());
        double avgScore = roundToThreeDecimalPlaces(totalScore / grades.size());

        Map<String, Double> overallStats = new HashMap<>();
        overallStats.put("avgCredit", avgCredit);
        overallStats.put("avgScore", avgScore);
        overallStats.put("totalCredits", totalCredits); // 전체 학점 누적

        return overallStats;
    }

    private double roundToThreeDecimalPlaces(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}
