package org.example.taskprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentTaskRunner {

    private final int maxConcurrency;
    private final int maxRetries;

    public ConcurrentTaskRunner(int maxConcurrency, int maxRetries) {
        this.maxConcurrency = maxConcurrency;
        this.maxRetries = maxRetries;
    }

    /**
     * 주어진 작업 목록을 최대 maxConcurrency개 스레드로 병렬 실행한다.
     * 각 작업은 실패 시 최대 maxRetries번 재시도한다.
     *
     * @param tasks 작업 이름 -> Task 매핑
     * @return 각 작업 이름에 대한 TaskResult 목록
     */
    public List<TaskResult> runAll(Map<String, Task> tasks) {
        // TODO: 구현하세요.
        //  - ExecutorService로 maxConcurrency만큼 병렬 실행
        //  - 각 작업은 maxRetries 횟수만큼 재시도
        //  - 모든 작업 완료 후 결과 반환
        //  - ExecutorService 반드시 종료 처리
        ExecutorService executorService = Executors.newFixedThreadPool(maxConcurrency);
        List<Future<TaskResult>> futures = new ArrayList<>();

        for ( Map.Entry<String, Task> entry : tasks.entrySet() ){
            String taskName = entry.getKey();
            Task task = entry.getValue();

            Callable<TaskResult> callable = () -> {
                Exception lastException = null;
                for (int i = 1; i<=maxRetries + 1; i++){
                    try {
                        task.execute();
                        return TaskResult.success(taskName, i);
                    }catch (Exception e){
                        lastException = e;
                    }
                }
                return TaskResult.failed(taskName, lastException.getMessage(),maxRetries + 1);
            };
            futures.add(executorService.submit(callable));
        }

        List<TaskResult> results = new ArrayList<>();
        try{
            for (Future<TaskResult> f : futures){
                try {
                    results.add(f.get());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }finally {
            executorService.shutdown();
        }

        return results;
    }
}
