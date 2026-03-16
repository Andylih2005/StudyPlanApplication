package com.example.StudyPlanApplication.controller;

import com.example.StudyPlanApplication.entity.RecommendationDTO;
import com.example.StudyPlanApplication.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/recommendations")
@CrossOrigin
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public List<RecommendationDTO> getRecommendations(@RequestParam String tag, @RequestParam String userID) {
        try {
            return recommendationService.fetchRecommendedTasks(tag);
        } catch (IOException e) {
            return List.of(); // 或使用錯誤 DTO
        }
    }

}
