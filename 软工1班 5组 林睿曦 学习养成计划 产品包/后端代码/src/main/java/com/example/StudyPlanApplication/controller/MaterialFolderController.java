package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.MaterialFolder;
import com.example.StudyPlanApplication.service.MaterialFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/folders")
public class MaterialFolderController {

    @Autowired
    private MaterialFolderService folderService;

    @GetMapping("/{userID}")
    public List<MaterialFolder> getUserFolders(@PathVariable String userID) {
        return folderService.getFoldersByUser(userID);
    }

    @PostMapping
    public void createFolder(@RequestBody MaterialFolder folder) {
        folderService.createFolder(folder);
    }

    @DeleteMapping("/{folderID}")
    public void deleteFolder(@PathVariable int folderID) {
        folderService.deleteFolder(folderID);
    }
}
