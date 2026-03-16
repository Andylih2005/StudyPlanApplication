package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.TaskCompletion;
import com.example.StudyPlanApplication.mapper.TaskCompletionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCompletionServiceImpl implements TaskCompletionService {
    @Autowired
    private TaskCompletionMapper mapper;

    @Override
    public int addCompletion(TaskCompletion taskCompletion) {
        int res=mapper.insert(taskCompletion);
        System.out.println(res);
        return res;
    }
    @Override
    public TaskCompletion getCompletion(int taskID, String userID) {
        return mapper.selectById(taskID, userID);
    }

    @Override
    public List<TaskCompletion> getUserCompletions(String userID) {
        return mapper.selectByUser(userID);
    }
    @Override
    public List<TaskCompletion> getUserCompletionsByTag(String userID, String tag){
        return mapper.findTasksCompletionsByTag(userID,tag);
    }

    @Override
    public int deleteCompletion(int taskID, String userID) {
        return mapper.delete(taskID, userID);
    }
    @Override
    public List<TaskCompletion> getUserCompletionsByDate(String userID, String date) {
        return mapper.selectByUserAndDate(userID, date);
    }
}
