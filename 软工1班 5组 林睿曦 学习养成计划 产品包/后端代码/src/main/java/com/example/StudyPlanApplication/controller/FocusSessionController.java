package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.FocusSession;
import com.example.StudyPlanApplication.service.FocusSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/focus-sessions")
public class FocusSessionController {

    @Autowired
    private FocusSessionService focusSessionService;

    @PostMapping
    public String createSession(@RequestBody FocusSession session) {
        session.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(session.getDurationSeconds());
        focusSessionService.addSession(session);
        return "Session created successfully.";
    }

    @GetMapping("/user/{userID}")
    public List<FocusSession> getSessionsByUser(@PathVariable String userID) {
        return focusSessionService.getSessionsByUser(userID);
    }

    @GetMapping("/{id}")
    public FocusSession getSessionById(@PathVariable Long id) {
        return focusSessionService.getSessionById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteSession(@PathVariable Long id) {
        focusSessionService.deleteSession(id);
        return "Session deleted successfully.";
    }
}