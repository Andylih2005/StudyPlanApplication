package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.Util.PdfUtil;
import com.example.StudyPlanApplication.entity.*;
import com.example.StudyPlanApplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@CrossOrigin // 開啟跨域（根據需要設置）
public class OpenAIController {

    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private TaskCompletionService taskCompletionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StudyMaterialService studyMaterialService;
    @Autowired
    private MaterialFolderService materialFolderService;
    @Autowired
    private MaterialProgressService materialProgressService;
    private PdfUtil pdfUtil;
    @PostMapping("/{userID}")
    public ResponseEntity<String> TaskCompleteAI(@PathVariable String userID,@RequestParam(value = "tag", required = false) String tag) throws Exception {
        List<Task> tasks;
        List<TaskCompletion> completes;
        if (!tag.equalsIgnoreCase("all")) {
            tasks = taskService.findTasksByTag(userID, tag);
            completes = taskCompletionService.getUserCompletionsByTag(userID, tag);
        } else {
            tasks = taskService.getTasksByUserId(userID);
            completes = taskCompletionService.getUserCompletions(userID);
        }
        StringBuilder message = new StringBuilder();
        for (Task task : tasks) {
            message.append(task.toString());
        }
        if (!tasks.isEmpty()) {
            message.append("，前面那些是我未完成的任务");
        }
        if (!completes.isEmpty()) {
            message.append("，这句话的后面是我已经完成的任务");
        }
        for (TaskCompletion complete : completes) {
            message.append(complete.toString());
        }
        message.append("，帮我生成任务完成情况报告，我更专注完成了哪个方面的学习，并且对于我即将需要完成的任务进行一个规划以及完成的建议，并且对于我之后可能学习的方向也做一个建议");
        ResponseEntity<String> result = chat(message.toString());
        String content = String.valueOf(result.getBody());
        List<MaterialFolder> find = materialFolderService.getFoldersByUser(userID);
        boolean finded = false;
        int folderid = 0;
        for (MaterialFolder temp : find) {
            if (tag.equalsIgnoreCase(temp.getFolderName())) {
                finded = true;
                folderid = temp.getFolderID();
                break;
            }
        }
        StudyMaterial tempmaterial = new StudyMaterial();
        tempmaterial.setFileData(PdfUtil.generatePdfFromString(content));
        if (tag.equalsIgnoreCase("all")) {
            tempmaterial.setTitle("AI报告");
            tempmaterial.setFileName("AI报告.pdf");
        } else {
            tempmaterial.setTitle(tag + "AI报告");
            tempmaterial.setFileName(tag + "AI报告.pdf");
        }
        tempmaterial.setUserID(userID);
        tempmaterial.setFileType("application/pdf");
        if (finded) {
            tempmaterial.setFolderID(folderid);
        }
        int materialID=studyMaterialService.upload(tempmaterial);
        MaterialProgress tt=new MaterialProgress();
        tt.setProgressType("pdf");
        tt.setMaterialID(materialID);
        tt.setProgress(0.0);
        tt.setUserID(userID);
        tt.setLastViewedAt(LocalDateTime.now());
        materialProgressService.updateProgress(tt);
        return result;
    }
    private ResponseEntity<String> chat(String request) throws Exception {
        String response = openAIService.getCompletion(request);
        return ResponseEntity.ok(response);
    }
}
