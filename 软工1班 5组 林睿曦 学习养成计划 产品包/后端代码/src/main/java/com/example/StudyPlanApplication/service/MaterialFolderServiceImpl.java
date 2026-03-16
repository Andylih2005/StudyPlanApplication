package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.MaterialFolder;
import com.example.StudyPlanApplication.mapper.MaterialFolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialFolderServiceImpl implements MaterialFolderService {
    @Autowired
    private MaterialFolderMapper folderMapper;
    @Override
    public List<MaterialFolder> getFoldersByUser(String userID) {
        return folderMapper.findByUserID(userID);
    }
    @Override
    public void createFolder(MaterialFolder folder) {
        folderMapper.insert(folder);
    }
    @Override
    public void deleteFolder(int folderID) {
        folderMapper.deleteById(folderID);
    }
}
