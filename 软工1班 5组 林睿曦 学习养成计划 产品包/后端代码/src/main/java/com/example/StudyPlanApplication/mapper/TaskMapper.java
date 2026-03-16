package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.Task;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskMapper {

    @Insert("INSERT INTO Task(userID, title, content, priority, deadline, uploaddate, next_reminder_at, tag) VALUES(#{userID}, #{title}, #{content}, #{priority}, #{deadline}, #{uploaddate}, #{NextReminderAt}, #{tag})")
    @Options(useGeneratedKeys = true, keyProperty = "taskID")
    int insertTask(Task task);

    @Update("UPDATE Task SET title=#{title}, content=#{content}, priority=#{priority}, deadline=#{deadline}, next_reminder_at=#{NextReminderAt}, tag=#{tag} WHERE taskID=#{taskID}")
    int updateTask(Task task);

    @Delete("DELETE FROM Task WHERE taskID=#{taskID}")
    int deleteTask(@Param("taskID") Integer taskID);

    @Select("SELECT * FROM Task WHERE taskID=#{taskID}")
    Task selectTaskById(@Param("taskID") Integer taskID);

    @Select("SELECT * FROM Task WHERE userID=#{userID}")
    List<Task> selectTasksByUserId(@Param("userID") String userID);

    @Select("SELECT * FROM Task WHERE userID = #{userID} AND DATE(deadline) = #{date}")
    List<Task> selectByUserAndDate(@Param("userID") String userID, @Param("date") String date);

    @Select("SELECT * FROM Task WHERE userID = #{userID} AND deadline BETWEEN #{start} AND #{end}")
    List<Task> getTasksByUserAndDateRange(@Param("userID") String userID, @Param("start") String start, @Param("end") String end);

    @Select("SELECT * FROM Task WHERE next_reminder_at <= #{now} AND userID=#{userID}")
    List<Task> findTasksToRemind(@Param("userID") String userID,@Param("now") LocalDateTime now);

    @Select("SELECT * FROM Task WHERE tag LIKE CONCAT('%', #{tag}, '%') AND userID=#{userID}")
    List<Task> findTasksByTag(@Param("userID") String userID,@Param("tag") String tag);

    @Update("UPDATE task SET next_reminder_at = #{nextReminderAt} WHERE taskID = #{taskID}")
    void updateTaskReminderTime(@Param("taskID") Integer taskID, @Param("nextReminderAt") LocalDateTime nextReminderAt);
}
