package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.MaterialProgress;
import com.example.StudyPlanApplication.mapper.MaterialProgressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialProgressService {

    @Autowired
    private MaterialProgressMapper progressMapper;

    public void updateProgress(MaterialProgress progress) {
        progress.setProgressType(mapFileTypeToProgressType(progress.getProgressType()));
        progressMapper.upsertProgress(progress);
    }

    public MaterialProgress getProgress(String userID, Integer materialID) {
        return progressMapper.getProgress(userID, materialID);
    }

    public List<MaterialProgress> getUserAllProgress(String userID) {
        return progressMapper.getAllProgressByUser(userID);
    }

    public int deleteProgress(int materialID){
        return progressMapper.deleteprogress(materialID);
    }
    private String mapFileTypeToProgressType(String fileType) {
        if (fileType == null) return "unknown";
        if (fileType.contains("pdf")) return "pdf";
        if (fileType.contains("word")) return "word";
        if (fileType.contains("powerpoint") || fileType.contains("presentation")) return "ppt";
        if (fileType.startsWith("video")) return "video";
        if (fileType.startsWith("text/html")) return "url";
        return "other";
    }
}
