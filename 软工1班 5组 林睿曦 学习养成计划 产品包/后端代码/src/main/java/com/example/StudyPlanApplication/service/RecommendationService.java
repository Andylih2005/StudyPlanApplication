package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.RecommendationDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    public List<RecommendationDTO> fetchRecommendedTasks(String tag) throws IOException {
        List<String> suffixes = List.of("教程", "复习", "笔记", "课程", "教学", "题目", "作业");
        List<String> learningFilterWords = List.of("题", "习题", "作业", "笔记", "知识", "课", "教学");

        List<RecommendationDTO> result = new ArrayList<>();

        for (String suffix : suffixes) {
            // 改进关键词：增加括号让 Bing 理解 OR
            String keyword = tag + " " + suffix;
            String query = URLEncoder.encode(keyword, "UTF-8");
            String url = "https://www.bing.com/search?q=" + query;

            System.out.println("正在访问：" + url);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122 Safari/537.36")
                    .timeout(10000)
                    .get();

            Elements links = doc.select("li.b_algo h2 a");
            for (Element link : links) {
                String title = link.text();
                String href = link.absUrl("href");

                boolean isLearningRelated = learningFilterWords.stream().anyMatch(title::contains);
                boolean isFromGoodSource = href.contains("zhihu.com") || href.contains("bilibili.com") || href.contains("moe.gov.cn");

                if ((isLearningRelated || isFromGoodSource) && !title.isEmpty() && !href.isEmpty()) {
                    RecommendationDTO dto = new RecommendationDTO();
                    dto.setTitle(title);
                    dto.setUrl(href);
                    dto.setSource(suffix); // 不需要放整个 keyword
                    result.add(dto);
                }

                if (result.size() >= 10) break;
            }

            if (result.size() >= 10) break;
        }

        return result;
    }

}

