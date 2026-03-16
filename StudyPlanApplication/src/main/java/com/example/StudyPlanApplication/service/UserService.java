package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.User;

public interface UserService {
    User getUserById(String userID);
    int addUser(User user);
    User login(String userID, String password);
}
