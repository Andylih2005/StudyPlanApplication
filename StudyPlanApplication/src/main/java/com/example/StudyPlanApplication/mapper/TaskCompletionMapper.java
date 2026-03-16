package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.Task;
import com.example.StudyPlanApplication.entity.TaskCompletion;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskCompletionMapper {

    @Insert("INSERT INTO TaskCompletion (taskID, userID, completedAt, title, deadline, content, tag) " +
            "VALUES (#{taskID}, #{userID}, #{completedAt}, #{title}, #{deadline}, #{content}, #{tag})")
    int insert(TaskCompletion taskCompletion);

    @Select("SELECT * FROM TaskCompletion WHERE taskID = #{taskID} AND userID = #{userID}")
    TaskCompletion selectById(@Param("taskID") int taskID, @Param("userID") String userID);

    @Select("SELECT * FROM TaskCompletion WHERE userID = #{userID}")
    List<TaskCompletion> selectByUser(String userID);

    @Delete("DELETE FROM TaskCompletion WHERE taskID = #{taskID} AND userID = #{userID}")
    int delete(@Param("taskID") int taskID, @Param("userID") String userID);

    @Select("SELECT * FROM TaskCompletion WHERE userID = #{userID} AND DATE(completedAt) = #{date}")
    List<TaskCompletion> selectByUserAndDate(@Param("userID") String userID, @Param("date") String date);

    @Select("SELECT * FROM TaskCompletion WHERE userID = #{userID} AND completedAt BETWEEN #{start} AND #{end}")
    List<TaskCompletion> getCompletionsByUserAndDateRange(@Param("userID") String userID, @Param("start") String start, @Param("end") String end);

    @Select("SELECT * FROM TaskCompletion WHERE tag LIKE CONCAT('%', #{tag}, '%') AND userID=#{userID}")
    List<TaskCompletion> findTasksCompletionsByTag(@Param("userID") String userID,@Param("tag") String tag);
}
