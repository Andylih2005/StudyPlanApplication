package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.StudyMaterial;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudyMaterialMapper {



    @Insert("INSERT INTO StudyMaterial (userID, title, fileName, fileType, fileData, tags,url,folderID) " +
            "VALUES (#{userID}, #{title}, #{fileName}, #{fileType}, #{fileData}, #{tags},#{url},#{folderID})")
    @Options(useGeneratedKeys = true, keyProperty = "materialID")
    int insertMaterial(StudyMaterial material);

    @Select("SELECT * FROM StudyMaterial WHERE materialID = #{materialID}")
    StudyMaterial findById(int materialID);

    @Select("SELECT materialID, userID, title, fileName, fileType, tags, url FROM StudyMaterial WHERE userID = #{userID}")
    List<StudyMaterial> findByUser(String userID);

    @Delete("DELETE FROM StudyMaterial WHERE materialID = #{materialID}")
    int deleteById(int materialID);

    @Select("SELECT materialID, userID, title, fileName, fileType, tags, url FROM StudyMaterial WHERE userID = #{userID} AND folderID = #{folderID}")
    List<StudyMaterial> getMaterialsByFolder(@Param("userID") String userID, @Param("folderID") Integer folderID);

    @Select("""
    SELECT * FROM StudyMaterial
    WHERE userID = #{userID}
    AND folderID = #{folderID}
    ORDER BY materialID DESC
    LIMIT #{limit} OFFSET #{offset}
""")
    List<StudyMaterial> getMaterialsByFolderPaged(
            @Param("userID") String userID,
            @Param("folderID") Integer folderID,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Select("SELECT materialID, userID, title, fileName, fileType, tags, url FROM StudyMaterial WHERE userID = #{userID} AND folderID IS NULL")
    List<StudyMaterial> selectByUserAndFolderIsNull(String userID);

    @Select("SELECT * FROM StudyMaterial WHERE userID = #{userID} AND title LIKE CONCAT('%', #{title}, '%')")
    List<StudyMaterial> findByUserAndTitle(@Param("userID") String userID, @Param("title") String title);
}
