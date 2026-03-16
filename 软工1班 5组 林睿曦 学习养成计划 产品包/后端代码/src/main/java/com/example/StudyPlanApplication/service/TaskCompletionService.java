package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.TaskCompletion;

import java.util.List;

public interface TaskCompletionService {
    int addCompletion(TaskCompletion taskCompletion);
    TaskCompletion getCompletion(int taskID, String userID);
    List<TaskCompletion> getUserCompletions(String userID);
    int deleteCompletion(int taskID, String userID);
    List<TaskCompletion> getUserCompletionsByDate(String userID, String date);
    List<TaskCompletion> getUserCompletionsByTag(String userID, String tag);
}
