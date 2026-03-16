package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.MaterialProgress;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MaterialProgressMapper {
    @Insert("""
        INSERT INTO MaterialProgress (userID, materialID, progress, progressType, lastViewedAt)
        VALUES (#{userID}, #{materialID}, #{progress}, #{progressType}, NOW())
        ON DUPLICATE KEY UPDATE
            progress = #{progress},
            progressType = #{progressType},
            lastViewedAt = NOW()
    """)
    void upsertProgress(MaterialProgress progress);

    // 查询某个资料的进度
    @Select("""
        SELECT userID, materialID, progress, progressType, lastViewedAt
        FROM MaterialProgress
        WHERE userID = #{userID} AND materialID = #{materialID}
    """)
    MaterialProgress getProgress(@Param("userID") String userID,
                                 @Param("materialID") Integer materialID);

    // 查询某个用户的所有进度（比如用于“学习概况”）
    @Select("""
        SELECT userID, materialID, progress, progressType, lastViewedAt
        FROM MaterialProgress
        WHERE userID = #{userID}
        ORDER BY updatedAt DESC
    """)
    List<MaterialProgress> getAllProgressByUser(@Param("userID") String userID);

    @Delete("DELETE FROM MaterialProgress WHERE MaterialID=#{materialID}")
    int deleteprogress(@Param("materialID") int materialID);
}
