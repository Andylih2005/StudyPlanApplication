package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.StudyMaterial;

import java.io.IOException;
import java.util.List;

public interface StudyMaterialService {
    int upload(StudyMaterial material);
    StudyMaterial getMaterial(int id) throws IOException;
    List<StudyMaterial> listByUser(String userID);
    int delete(int id);
    List<StudyMaterial> listByUserandfolder(String userID,int folderID);
    List<StudyMaterial> getMaterialsByFolderPaged(String userID, Integer folderID, int page, int pageSize);
    List<StudyMaterial> listUncategorizedByUser(String userID);
    List<StudyMaterial> findByUserAndTitle(String userID,String title);
}