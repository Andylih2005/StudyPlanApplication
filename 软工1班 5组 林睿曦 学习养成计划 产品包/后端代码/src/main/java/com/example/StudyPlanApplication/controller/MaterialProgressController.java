package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.MaterialProgress;
import com.example.StudyPlanApplication.service.MaterialProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin
public class MaterialProgressController {

    @Autowired
    private MaterialProgressService progressService;

    @PostMapping("/update")
    public ResponseEntity<?> updateProgress(@RequestBody MaterialProgress progress) {
        progressService.updateProgress(progress);
        return ResponseEntity.ok(Map.of("message", "进度已更新"));
    }

    @GetMapping
    public MaterialProgress getProgress(@RequestParam String userID,
                                        @RequestParam Integer materialID) {
        return progressService.getProgress(userID, materialID);
    }

    @GetMapping("/user/{userID}")
    public List<MaterialProgress> getAllByUser(@PathVariable String userID) {
        return progressService.getUserAllProgress(userID);
    }
}