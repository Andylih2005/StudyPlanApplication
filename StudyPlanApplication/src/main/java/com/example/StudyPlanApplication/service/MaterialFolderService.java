package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.MaterialFolder;

import java.util.List;

public interface MaterialFolderService {

    List<MaterialFolder> getFoldersByUser(String userID);

    void createFolder(MaterialFolder folder);

    void deleteFolder(int folderID);
}
