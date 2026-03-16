package com.example.StudyPlanApplication.service;
import com.example.StudyPlanApplication.entity.User;
import com.example.StudyPlanApplication.mapper.UserMapper;
import com.example.StudyPlanApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserById(String userID){
        return userMapper.findByUserID(userID);
    }
    @Override
    public int addUser(User user) {
        return userMapper.insertUser(user);
    }
    @Override
    public User login(String userID, String password) {
        return userMapper.findByUserIDAndPassword(userID, password);
    }
}
