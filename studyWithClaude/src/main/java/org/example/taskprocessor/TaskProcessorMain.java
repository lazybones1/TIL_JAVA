package org.example.taskprocessor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TaskProcessorMain {

    public static void main(String[] args) {
        Map<String, Task> tasks = new LinkedHashMap<>();
        Random random = new Random();

        // 샘플 작업: 일부는 랜덤하게 실패
        for (int i = 1; i <= 6; i++) {
            final int id = i;
            tasks.put("task-" + id, () -> {
                Thread.sleep(100 + random.nextInt(200));
                if (random.nextDouble() < 0.7) {
                    throw new RuntimeException("task-" + id + " 처리 중 오류 발생");
                }
            });
        }

        ConcurrentTaskRunner runner = new ConcurrentTaskRunner(3, 2);
        List<TaskResult> results = runner.runAll(tasks);

        // TODO: results를 순회하며 성공/실패 여부, 작업 이름, 실패 원인 등을
        //       의미 있게 출력하세요.
        //       마지막에 전체 성공 수 / 실패 수 요약도 출력하세요.
        StringBuilder sb = new StringBuilder();
        int successCnt = 0, failedCnt = 0;
        for (TaskResult taskResult : results){
            sb.append(taskResult.getStatus().toString())
                    .append(" | ")
                    .append(taskResult.getTaskName());
            if (taskResult.isSuccess()){
                sb.append(" | ").append(taskResult.getFailResult());
                failedCnt++;
            }else{
                successCnt++;
            }
            sb.append(" | ").append(taskResult.getTryCount()).append("\n");
        }
        sb.append(successCnt + failedCnt).append(" | ").append(successCnt).append(" | ").append(failedCnt);
        System.out.print(sb.toString());
    }
}
