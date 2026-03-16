package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.ReminderIntervalConfig;
import com.example.StudyPlanApplication.mapper.ReminderIntervalConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderIntervalConfigServiceImpl implements ReminderIntervalConfigService{
    @Autowired
    ReminderIntervalConfigMapper mapper;
    @Override
    public ReminderIntervalConfig getreminder(int priority,String userID){
        return mapper.getByPriority(priority,userID);
    }
    @Override
    public int setreminder(int minuts,int priority,String userID){
        return mapper.setReminder(minuts,priority,userID);
    }
    @Override
    public void addReminderIntervalConfig(int priority, int intervalMinutes, String userID) {
        ReminderIntervalConfig config = new ReminderIntervalConfig(priority, intervalMinutes, userID);
        mapper.insert(config);
    }
    @Override
    public List<ReminderIntervalConfig> getReminderIntervalConfig(String userID){
        return mapper.getByuserID(userID);
    }
}
