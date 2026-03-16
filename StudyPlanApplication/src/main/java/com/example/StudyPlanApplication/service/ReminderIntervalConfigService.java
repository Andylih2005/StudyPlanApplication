package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.ReminderIntervalConfig;
import com.example.StudyPlanApplication.mapper.ReminderIntervalConfigMapper;

import java.util.List;

public interface ReminderIntervalConfigService {
    ReminderIntervalConfig getreminder(int priority,String userID);
    int setreminder(int minuts,int priority,String userID);
    List<ReminderIntervalConfig> getReminderIntervalConfig(String userID);
    void addReminderIntervalConfig(int priority, int intervalMinutes, String userID);
}
