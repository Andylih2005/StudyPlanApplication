package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.FocusSession;
import com.example.StudyPlanApplication.mapper.FocusSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FocusSessionServiceImpl implements FocusSessionService {

    @Autowired
    private FocusSessionMapper focusSessionMapper;

    @Override
    public void addSession(FocusSession session) {
        focusSessionMapper.insert(session);
    }

    @Override
    public List<FocusSession> getSessionsByUser(String userID) {
        return focusSessionMapper.findByUserID(userID);
    }

    @Override
    public FocusSession getSessionById(Long id) {
        return focusSessionMapper.findById(id);
    }

    @Override
    public void deleteSession(Long id) {
        focusSessionMapper.deleteById(id);
    }
}
