package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.StudyMaterial;
import com.example.StudyPlanApplication.mapper.StudyMaterialMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StudyMaterialServiceImpl implements StudyMaterialService {

    @Autowired
    private StudyMaterialMapper mapper;

    @Override
    public int upload(StudyMaterial material) {
        System.out.println("upload start");
        mapper.insertMaterial(material); // 会自动填充 material.materialID
        System.out.println("upload finish");
        return material.getMaterialID();
    }
    @Override
    public StudyMaterial getMaterial(int id) throws IOException {
        StudyMaterial material = mapper.findById(id);

        String fileType = material.getFileType();
        System.out.println("文件类型: " + fileType);

        // 先确保目录存在
        File tempDir = new File("temp");
        if (!tempDir.exists()) tempDir.mkdirs();
        File convertedDir = new File("converted");
        if (!convertedDir.exists()) convertedDir.mkdirs();

        if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(fileType) ||
                "application/msword".equalsIgnoreCase(fileType) ||
                "application/vnd.openxmlformats-officedocument.presentationml.presentation".equalsIgnoreCase(fileType) ||
                "application/vnd.ms-powerpoint".equalsIgnoreCase(fileType)) {
            File tempInput = new File(tempDir, material.getFileName());
            FileUtils.writeByteArrayToFile(tempInput, material.getFileData());
            System.out.println("tempInput exists: " + tempInput.exists() + ", size: " + tempInput.length());
            String pdfName = material.getFileName().replaceAll("\\.(docx?|pptx?)$", ".pdf");
            File tempOutput = new File(convertedDir, pdfName);

            try {
                OfficeToPdfUtil.convertToPdf(tempInput, tempOutput);
                byte[] pdfData = FileUtils.readFileToByteArray(tempOutput);

                material.setFileType("application/pdf");
                material.setFileName(pdfName);
                material.setFileData(pdfData);
                System.out.println("change successful");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("转换 PDF 失败: " + e.getMessage(), e);
            }finally {
                if (tempInput.exists()) {
                    tempInput.delete();
                }
                if (tempOutput != null && tempOutput.exists()) {
                    tempOutput.delete();
                }
            }
        }

        return material;
    }


    @Override
    public List<StudyMaterial> listByUser(String userID) {
        return mapper.findByUser(userID);
    }

    @Override
    public int delete(int id) {
        return mapper.deleteById(id);
    }
    @Override
    public List<StudyMaterial> listByUserandfolder(String userID,int folderID){
        return mapper.getMaterialsByFolder(userID,folderID);
    }
    @Override
    public List<StudyMaterial> getMaterialsByFolderPaged(String userID, Integer folderID, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return mapper.getMaterialsByFolderPaged(userID, folderID, pageSize, offset);
    }
    public List<StudyMaterial> listUncategorizedByUser(String userID) {
        return mapper.selectByUserAndFolderIsNull(userID);
    }
    @Override
    public List<StudyMaterial> findByUserAndTitle(String userID,String title){
        return mapper.findByUserAndTitle(userID,title);
    }
}

