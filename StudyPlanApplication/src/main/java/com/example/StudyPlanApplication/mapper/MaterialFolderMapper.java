package com.example.StudyPlanApplication.mapper;

import com.example.StudyPlanApplication.entity.MaterialFolder;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaterialFolderMapper {

    @Insert("INSERT INTO MaterialFolder(userID, folderName) VALUES(#{userID}, #{folderName})")
    int insert(MaterialFolder folder);

    @Select("SELECT * FROM MaterialFolder WHERE userID = #{userID}")
    List<MaterialFolder> findByUserID(String userID);

    @Delete("DELETE FROM MaterialFolder WHERE folderID = #{folderID}")
    int deleteById(int folderID);
}
