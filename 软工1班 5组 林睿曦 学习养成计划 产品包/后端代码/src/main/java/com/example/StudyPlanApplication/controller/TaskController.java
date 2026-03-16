package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.Task;
import com.example.StudyPlanApplication.entity.TaskCompletion;
import com.example.StudyPlanApplication.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    private void validTask(Task task)
    {
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be NULL");
        }
        if (task.getPriority() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Priority cannot be NULL");
        }
        if (task.getDeadline() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline cannot be NULL");
        }
    }
    @PostMapping
    public String addTask(@RequestBody Task task) {
        validTask(task);
        LocalDateTime now = LocalDateTime.now();
        Duration interval = taskService.getReminderInterval(task.getPriority(),task.getUserID());
        task.setNextReminderAt(now.plus(interval));
        int result = taskService.addTask(task);
        return result > 0 ? "Task added successfully" : "Failed to add task";
    }
    @PutMapping("/{taskID}")
    public String updateTask(@PathVariable Integer taskID, @RequestBody Task task) {
        task.setTaskID(taskID);
        validTask(task);
        LocalDateTime now = LocalDateTime.now();
        Duration interval = taskService.getReminderInterval(task.getPriority(),task.getUserID());
        task.setNextReminderAt(now.plus(interval));
        int result = taskService.updateTask(task);
        return result > 0 ? "Task updated successfully" : "Failed to update task";
    }

    @DeleteMapping("/{taskID}")
    public String deleteTask(@PathVariable Integer taskID) {
        System.out.println("Deleting");
        int result = taskService.deleteTask(taskID);
        return result > 0 ? "Task deleted successfully" : "Failed to delete task";
    }

    @GetMapping("/{taskID}")
    public Task getTaskById(@PathVariable Integer taskID) {
        return taskService.getTaskById(taskID);
    }

    @GetMapping("/user/{userID}")
    public List<Task> getTasksByUserId(@PathVariable String userID) {
        return taskService.getTasksByUserId(userID);
    }

    @GetMapping("/tag/{tag}/{userID}")
    public List<Task> getTasksByTag(@PathVariable String tag,@PathVariable String userID){
        System.out.println(tag+" "+userID);
        return taskService.findTasksByTag(userID,tag);
    }

    @GetMapping("/{userID}/date")
    public List<Task> getUserCompletionsByDate(
            @PathVariable String userID,
            @RequestParam String date // 格式：YYYY-MM-DD
    ) {
        return taskService.getUserTasksByDate(userID, date);
    }

    @GetMapping("/remind/{userID}")
    public List<Task> getTasksToRemind(@PathVariable String userID) {
        LocalDateTime now = LocalDateTime.now();
        return taskService.findTasksToRemind(userID, now);
    }
}
