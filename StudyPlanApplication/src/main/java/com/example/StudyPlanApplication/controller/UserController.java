package com.example.StudyPlanApplication.controller;
import com.example.StudyPlanApplication.entity.User;
import com.example.StudyPlanApplication.service.ReminderIntervalConfigService;
import com.example.StudyPlanApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReminderIntervalConfigService reminderIntervalConfigService;
    @GetMapping("/{id}")
    public User getUserByID(@PathVariable String id) {
        return userService.getUserById(id);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User found = userService.login(user.getUserID(), user.getPassword());
        if (found != null) {
            return ResponseEntity.ok(found); // 登录成功返回用户信息
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("账号或密码错误");
        }
    }
    @PostMapping("/register")
    public int insertUser(@RequestBody User user){
        int result=userService.addUser(user);
        if (result>0){
            reminderIntervalConfigService.addReminderIntervalConfig(1,1440,user.getUserID());
            reminderIntervalConfigService.addReminderIntervalConfig(2,360,user.getUserID());
            reminderIntervalConfigService.addReminderIntervalConfig(3,180,user.getUserID());
            reminderIntervalConfigService.addReminderIntervalConfig(4,60,user.getUserID());
            reminderIntervalConfigService.addReminderIntervalConfig(5,30,user.getUserID());
        }
        return result;
    }
}
