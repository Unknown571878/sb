package com.example.demo.service;

import com.example.demo.entity.Schedule;
import com.example.demo.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 특정 날짜에 해당하는 일정 목록을 조회
    public List<Schedule> getSchedulesByDate(String year, String month, String day) {
        return scheduleRepository.findBySDayAndSMonthAndSYear(year, month, day);
    }
}
