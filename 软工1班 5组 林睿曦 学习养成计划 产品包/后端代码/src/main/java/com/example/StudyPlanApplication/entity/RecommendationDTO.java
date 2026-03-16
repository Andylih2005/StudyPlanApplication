package com.example.StudyPlanApplication.entity;

public class RecommendationDTO {
    private String title;
    private String url;
    private String source; // 原始 keyword，如 "物理 作业"

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
// Getter 和 Setter
}
