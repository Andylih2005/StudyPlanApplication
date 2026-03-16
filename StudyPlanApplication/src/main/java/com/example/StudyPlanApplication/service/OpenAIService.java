package com.example.StudyPlanApplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAIService {

    public static String URL="https://api.deepseek.com/chat/completions";
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS) // TCP 连接超时时间
            .readTimeout(60, TimeUnit.SECONDS)    // 读取响应数据的超时时间
            .writeTimeout(60, TimeUnit.SECONDS)   // 写入请求体的超时时间
            .build();
    public static final MediaType JSON= MediaType.get("application/json; charset=utf-8");
    public String getCompletion(String message) throws Exception {
        String requestBody = "{\n" +
                "  \"model\": \"deepseek-chat\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + message + "\"}\n" +
                "  ]\n" +
                "}";

        RequestBody body = RequestBody.create(requestBody, JSON);
        Request request = new Request.Builder()
                .addHeader("Authorization", "Bearer " + "sk-29c04c84a96d42359289fbfff1fde539")//Bearer 必须带后面是自己的key
                .url(URL)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseBody);
        String content = rootNode
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();
        System.out.println(content);
        return content;
    }

}
