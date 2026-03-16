package com.example.StudyPlanApplication.entity;

public class ReminderIntervalConfig {
    private int priority;
    private int intervalMinutes;
    private String userID;

    public ReminderIntervalConfig() {
    }

    public ReminderIntervalConfig(int priority, int intervalMinutes, String userID) {
        this.priority = priority;
        this.intervalMinutes = intervalMinutes;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getIntervalMinutes() {
        return intervalMinutes;
    }

    public void setIntervalMinutes(int intervalMinutes) {
        this.intervalMinutes = intervalMinutes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
// getters and setters
}
