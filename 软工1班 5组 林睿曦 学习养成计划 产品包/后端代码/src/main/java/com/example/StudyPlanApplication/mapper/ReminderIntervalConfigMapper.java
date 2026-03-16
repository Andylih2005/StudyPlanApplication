package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.ReminderIntervalConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReminderIntervalConfigMapper {

    @Insert("INSERT INTO reminder_interval_config (priority, interval_minutes, userID) " +
            "VALUES (#{priority}, #{intervalMinutes}, #{userID})")
    void insert(ReminderIntervalConfig config);

    @Select("SELECT * FROM reminder_interval_config WHERE priority = #{priority} AND userID=#{userID}")
    ReminderIntervalConfig getByPriority(@Param("priority") int priority,@Param("userID") String userID);

    @Select("SELECT * FROM reminder_interval_config WHERE userID=#{userID}")
    List<ReminderIntervalConfig> getByuserID(String userID);
    @Update("UPDATE reminder_interval_config SET interval_minutes = #{intervalMinutes} WHERE priority = #{priority} AND userID=#{userID}")
    int setReminder(@Param("intervalMinutes") int intervalMinutes, @Param("priority") int priority, @Param("userID") String userID);
}
