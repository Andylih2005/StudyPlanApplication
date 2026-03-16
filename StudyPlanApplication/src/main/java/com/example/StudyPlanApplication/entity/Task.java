package com.example.StudyPlanApplication.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class Task {
    private Integer taskID;
    private String userID;
    private String title;
    private String content;
    private Integer priority;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Date uploaddate;
    private LocalDateTime NextReminderAt;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public LocalDateTime getNextReminderAt() {
        return NextReminderAt;
    }

    public void setNextReminderAt(LocalDateTime nextReminderAt) {
        NextReminderAt = nextReminderAt;
    }

    public Date getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(Date uploaddate) {
        this.uploaddate = uploaddate;
    }

    // getters and setters
    public Integer getTaskID() { return taskID; }
    public void setTaskID(Integer taskID) { this.taskID = taskID; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public String toString(){
        return "任务标题："+title+"任务内容："+content+"开始时间"+uploaddate.toString()+"结束时间："+deadline.toString();
    }
}
