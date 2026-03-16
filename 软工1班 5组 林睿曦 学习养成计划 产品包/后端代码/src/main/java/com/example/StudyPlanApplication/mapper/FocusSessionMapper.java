package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.FocusSession;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FocusSessionMapper {

    @Insert("INSERT INTO focus_sessions (userID, duration_seconds, completed_at, tag, title, content, starttime) " +
            "VALUES (#{userID}, #{durationSeconds}, #{completedAt}, #{tag}, #{title}, #{content}, #{starttime})")
    @Options(useGeneratedKeys = true, keyProperty = "sessionId")
    int insert(FocusSession session);

    @Select("SELECT * FROM focus_sessions WHERE userID = #{userID} ORDER BY completed_at DESC")
    List<FocusSession> findByUserID(String userID);

    @Select("SELECT * FROM focus_sessions WHERE session_id = #{id}")
    FocusSession findById(Long id);

    @Delete("DELETE FROM focus_sessions WHERE session_id = #{id}")
    int deleteById(Long id);
}
