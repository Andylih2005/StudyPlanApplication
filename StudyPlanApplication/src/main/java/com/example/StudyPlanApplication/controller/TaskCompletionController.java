package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.TaskCompletion;
import com.example.StudyPlanApplication.service.TaskCompletionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/completion")
public class TaskCompletionController {

    private final TaskCompletionService service;

    public TaskCompletionController(TaskCompletionService service) {
        this.service = service;
    }

    @PostMapping
    public int addCompletion(@RequestBody TaskCompletion completion) {
        return service.addCompletion(completion);
    }

    @GetMapping("/{userID}")
    public List<TaskCompletion> getUserCompletions(@PathVariable String userID) {
        return service.getUserCompletions(userID);
    }
    @GetMapping("/{userID}/date")
    public List<TaskCompletion> getUserCompletionsByDate(
            @PathVariable String userID,
            @RequestParam String date // 格式：YYYY-MM-DD
    ) {
        return service.getUserCompletionsByDate(userID, date);
    }
    @GetMapping("/tag/{tag}/{userID}")
    public List<TaskCompletion> getUserCompletionsByTag(@PathVariable String userID,@PathVariable String tag){
        return service.getUserCompletionsByTag(userID,tag);
    }
    @DeleteMapping
    public int deleteCompletion(@RequestParam int taskID, @RequestParam String userID) {
        return service.deleteCompletion(taskID, userID);
    }
}
