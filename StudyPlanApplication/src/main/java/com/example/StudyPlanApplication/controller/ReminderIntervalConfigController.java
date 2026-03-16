package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.ReminderIntervalConfig;
import com.example.StudyPlanApplication.service.ReminderIntervalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminder")
@CrossOrigin
public class ReminderIntervalConfigController {
    @Autowired
    private ReminderIntervalConfigService service;
    @PostMapping("/add")
    public ResponseEntity<String> addConfig(@RequestBody ReminderIntervalConfig config) {
        service.addReminderIntervalConfig(config.getPriority(), config.getIntervalMinutes(), config.getUserID());
        return ResponseEntity.ok("新增成功");
    }
    @PutMapping("/{userID}")
    public ResponseEntity<String> setReminderTime(
            @RequestParam int minus,
            @RequestParam int priority,
            @PathVariable String userID) {

        int res = service.setreminder(minus, priority, userID);
        if (res > 0) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
    }
    @GetMapping("/{userID}")
    public ResponseEntity<List<ReminderIntervalConfig>> getConfig(@PathVariable String userID) {
        List<ReminderIntervalConfig> config = service.getReminderIntervalConfig(userID);
        if (config != null) {
            return ResponseEntity.ok(config);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
