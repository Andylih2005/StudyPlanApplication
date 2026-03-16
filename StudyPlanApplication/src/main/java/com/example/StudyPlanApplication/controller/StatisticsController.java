package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.DailyStatsDTO;
import com.example.StudyPlanApplication.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/daily")
    public List<DailyStatsDTO> getStats(
            @RequestParam String userID,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        System.out.println("Matthew");
        return statisticsService.getDailyStats(userID, startDate, endDate);
    }
}

