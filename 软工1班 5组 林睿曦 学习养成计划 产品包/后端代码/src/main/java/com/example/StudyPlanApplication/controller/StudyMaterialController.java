package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.StudyMaterial;
import com.example.StudyPlanApplication.service.MaterialProgressService;
import com.example.StudyPlanApplication.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/material")
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

    @Autowired
    private MaterialProgressService materialProgressService;

    @GetMapping("/search")
    public List<StudyMaterial> searchByTitle(@RequestParam String title,
                                             @RequestParam String userID) {
        return studyMaterialService.findByUserAndTitle(userID, title);
    }

    @GetMapping("/uncategorized")
    public List<StudyMaterial> getUncategorizedMaterials(@RequestParam String userID) {
        return studyMaterialService.listUncategorizedByUser(userID);
    }

    @PostMapping("/save-url")
    public ResponseEntity<Map<String, Object>> saveUrlMaterial(@RequestParam("userID") String userID,
                                                               @RequestParam("title") String title,
                                                               @RequestParam("url") String url,
                                                               @RequestParam(value = "tags", required = false) String tags,
                                                               @RequestParam(value="folderID",required = false) Integer folderID) {
        StudyMaterial material = new StudyMaterial();
        material.setUserID(userID);
        material.setTitle(title);
        material.setUrl(url);
        material.setTags(tags);
        material.setFolderID(folderID);

        int materialID = studyMaterialService.upload(material); // 新建完返回 ID
        return ResponseEntity.ok(Map.of(
                "message", "URL 資料儲存成功",
                "materialID", materialID
        ));
    }
    // 上传单个学习资料
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("userID") String userID,
                                                      @RequestParam("title") String title,
                                                      @RequestParam(value = "tags", required = false) String tags,
                                                      @RequestParam(value="folderID",required = false) Integer folderID) {
        try {
            StudyMaterial material = new StudyMaterial();
            material.setUserID(userID);
            material.setTitle(title);
            material.setFileName(file.getOriginalFilename());
            material.setFileType(file.getContentType());
            material.setFileData(file.getBytes());
            material.setTags(tags);
            material.setFolderID(folderID);

            int materialID = studyMaterialService.upload(material); // 改成返回 materialID
            return ResponseEntity.ok(Map.of(
                    "message", "Upload successful",
                    "materialID", materialID
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "File processing error"));
        }
    }
    // 根据 userID 获取学习资料列表（不含文件内容）
    @GetMapping("/user/{userID}")
    public List<StudyMaterial> getMaterialsByUser(
            @PathVariable String userID,
            @RequestParam(required = false) Integer folderID) {
        if (folderID != null) {
            return studyMaterialService.listByUserandfolder(userID, folderID);
        } else {
            return studyMaterialService.listByUser(userID);
        }
    }
    @GetMapping("/{materialID}/content")
    public ResponseEntity<byte[]> getFileBlob(@PathVariable Integer materialID) throws IOException {
        StudyMaterial material = studyMaterialService.getMaterial(materialID);
        if (material == null || material.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = material.getFileName(); // e.g., "2-1导数的概念 2020.pdf"

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(material.getFileType()));

        // 设置 Content-Disposition（浏览器兼容的）
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String contentDisposition = String.format("inline; filename=\"%s\"; filename*=UTF-8''%s", "file.pdf", encodedFilename);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return new ResponseEntity<>(material.getFileData(), headers, HttpStatus.OK);
    }
    // 下载学习资料（包含二进制内容）
    @GetMapping("/download/{materialID}")
    public ResponseEntity<byte[]> download(@PathVariable int materialID) throws IOException {
        StudyMaterial material = studyMaterialService.getMaterial(materialID);
        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(material.getFileType()));
        headers.setContentDispositionFormData("attachment", material.getFileName());
        return new ResponseEntity<>(material.getFileData(), headers, HttpStatus.OK);
    }

    // 删除资料
    @DeleteMapping("/delete/{materialID}")
    public ResponseEntity<String> delete(@PathVariable int materialID) {
        int rows = studyMaterialService.delete(materialID);
        if (rows > 0) {
            materialProgressService.deleteProgress(materialID);
            return ResponseEntity.ok("Deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material not found");
        }
    }
    @GetMapping("/folderPaged")
    public List<StudyMaterial> getMaterialsByFolderPaged(
            @RequestParam String userID,
            @RequestParam Integer folderID,
            @RequestParam int page,
            @RequestParam int pageSize
    ) {
        return studyMaterialService.getMaterialsByFolderPaged(userID, folderID, page, pageSize);
    }
}
