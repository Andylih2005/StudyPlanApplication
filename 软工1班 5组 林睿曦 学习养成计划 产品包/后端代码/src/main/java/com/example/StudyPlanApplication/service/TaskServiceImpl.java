package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.ReminderIntervalConfig;
import com.example.StudyPlanApplication.entity.Task;
import com.example.StudyPlanApplication.mapper.ReminderIntervalConfigMapper;
import com.example.StudyPlanApplication.mapper.TaskMapper;
import com.example.StudyPlanApplication.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ReminderIntervalConfigMapper intervalConfigMapper;
    @Override
    public int addTask(Task task) {
        return taskMapper.insertTask(task);
    }

    @Override
    public int updateTask(Task task) {
        return taskMapper.updateTask(task);
    }

    @Override
    public int deleteTask(Integer taskID) {
        return taskMapper.deleteTask(taskID);
    }

    @Override
    public Task getTaskById(Integer taskID) {
        return taskMapper.selectTaskById(taskID);
    }

    @Override
    public List<Task> getTasksByUserId(String userID) {
        return taskMapper.selectTasksByUserId(userID);
    }

    @Override
    public List<Task> getUserTasksByDate(String userID, String date) {
        return taskMapper.selectByUserAndDate(userID, date);
    }
    @Override
    public List<Task> findTasksByTag(String userID, String tag){
        return taskMapper.findTasksByTag(userID,tag);
    }
    @Override
    public Duration getReminderInterval(int priority,String userID) {
        ReminderIntervalConfig config = intervalConfigMapper.getByPriority(priority,userID);
        if (config == null) {
            throw new IllegalArgumentException("Invalid priority: " + priority);
        }
        return Duration.ofMinutes(config.getIntervalMinutes());
    }
    @Override
    public int setReminder(int minuts,int priority,String userID){
        return intervalConfigMapper.setReminder(minuts,priority,userID);
    }
    @Override
    public List<Task> findTasksToRemind(String userID, LocalDateTime now) {
        List<Task> tasks = taskMapper.findTasksToRemind(userID,now);
        for (Task task : tasks) {
            System.out.println("remind");
            Duration interval = getReminderInterval(task.getPriority(),task.getUserID());
            LocalDateTime nextReminder = now.plus(interval);
            taskMapper.updateTaskReminderTime(task.getTaskID(), nextReminder);
            task.setNextReminderAt(nextReminder);
        }
        return tasks;
    }
}
