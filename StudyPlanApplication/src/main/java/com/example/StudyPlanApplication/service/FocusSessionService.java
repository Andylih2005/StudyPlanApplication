package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.FocusSession;

import java.util.List;

public interface FocusSessionService {
    void addSession(FocusSession session);
    List<FocusSession> getSessionsByUser(String userID);
    FocusSession getSessionById(Long id);
    void deleteSession(Long id);
}
