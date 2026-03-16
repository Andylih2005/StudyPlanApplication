package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    int addTask(Task task);
    int updateTask(Task task);
    int deleteTask(Integer taskID);
    Task getTaskById(Integer taskID);
    List<Task> getTasksByUserId(String userID);
    List<Task> getUserTasksByDate(String userID, String date);
    Duration getReminderInterval(int priority,String userID);
    List<Task> findTasksToRemind(String userID, LocalDateTime now);
    List<Task> findTasksByTag(String userID, String tag);
    int setReminder(int minuts,int priority,String userID);
}
