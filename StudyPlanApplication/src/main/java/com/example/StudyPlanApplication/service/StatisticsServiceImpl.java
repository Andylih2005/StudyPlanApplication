package com.example.StudyPlanApplication.service;

import com.example.StudyPlanApplication.entity.DailyStatsDTO;
import com.example.StudyPlanApplication.entity.Task;
import com.example.StudyPlanApplication.entity.TaskCompletion;
import com.example.StudyPlanApplication.mapper.TaskCompletionMapper;
import com.example.StudyPlanApplication.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service   ;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskCompletionMapper completionMapper;

    @Override
    public List<DailyStatsDTO> getDailyStats(String userID, String startDate, String endDate) {

        List<Task> tasks = taskMapper.getTasksByUserAndDateRange(userID, startDate, endDate);
        List<TaskCompletion> completions = completionMapper.getCompletionsByUserAndDateRange(userID, startDate, endDate);

        // 1. 按截止日期对 Task 进行分组（yyyy-MM-dd）
        Map<String, List<Task>> taskMap = tasks.stream()
                .collect(Collectors.groupingBy(t ->
                        t.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()
                ));

        // 2. 按任务 deadline 对完成记录进行分组（注意：使用 TaskCompletion 的 deadline）
        Map<String, List<TaskCompletion>> completionMap = completions.stream()
                .collect(Collectors.groupingBy(c ->
                        c.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()
                ));

        // 3. 整合所有出现过的日期
        Set<String> allDates = new TreeSet<>();
        allDates.addAll(taskMap.keySet());
        allDates.addAll(completionMap.keySet());

        // 4. 构造每日统计结果
        List<DailyStatsDTO> result = new ArrayList<>();

        for (String date : allDates) {
            List<Task> taskList = taskMap.getOrDefault(date, Collections.emptyList());
            List<TaskCompletion> compList = completionMap.getOrDefault(date, Collections.emptyList());

            // ⏱️ 计算平均耗时（完成时间 - deadline）单位：分钟
            double avgTime = compList.stream()
                    .filter(c -> c.getCompletedAt() != null && c.getDeadline() != null)
                    .mapToLong(c -> ((c.getDeadline().getTime()- c.getCompletedAt().getTime()) / (1000 * 60)))
                    .average()
                    .orElse(0);

            // 📊 构造返回 DTO
            DailyStatsDTO dto = new DailyStatsDTO();
            dto.setDate(date);
            dto.setTotalTasks(taskList.size() + compList.size());
            dto.setCompletedTasks(compList.size());
            dto.setCompletionRate(
                    (taskList.size() + compList.size()) == 0 ? 0 :
                            (double) compList.size() / (taskList.size() + compList.size())
            );
            dto.setAverageCompletionTime(avgTime);

            result.add(dto);
        }

        return result;
    }
}
