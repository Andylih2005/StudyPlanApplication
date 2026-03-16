package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE userID = #{userID}")
    User findByUserID(@Param("userID") String userID);

    @Insert("INSERT INTO users (userID, password) VALUES (#{userID}, #{password})")
    int insertUser(User user);

    @Select("SELECT * FROM users WHERE userID = #{userID} AND password = #{password}")
    User findByUserIDAndPassword(@Param("userID") String userID, @Param("password") String password);
}
