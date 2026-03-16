package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.DailyStatsDTO;

import java.util.List;

public interface StatisticsService {
    List<DailyStatsDTO> getDailyStats(String userID, String startDate, String endDate);
}
