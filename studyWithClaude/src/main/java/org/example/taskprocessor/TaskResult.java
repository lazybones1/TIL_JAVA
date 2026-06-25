package org.example.taskprocessor;


public class TaskResult {

    // TODO: 성공/실패 상태, 작업 이름, 실패 원인, 시도 횟수 등
    //       필요한 필드를 직접 설계하고 추가하세요.
    //       생성자, 팩토리 메서드, getter도 함께 설계하세요.
    private final String taskName;
    private final TaskStatus status;
    private final String failResult;
    private final int tryCount;

    private TaskResult(String taskName, TaskStatus status, String failResult, int tryCount) {
        this.taskName = taskName;
        this.status = status;
        this.failResult = failResult;
        this.tryCount = tryCount;
    }

    public static TaskResult success(String taskName, int tryCount){
        return new TaskResult(taskName, TaskStatus.SUCCESS, "", tryCount);
    }

    public static TaskResult failed(String taskName, String failResult, int tryCount){
        return new TaskResult(taskName, TaskStatus.FAILED, failResult, tryCount);
    }


    public String getTaskName() {
        return taskName;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getFailResult() {
        return failResult;
    }

    public int getTryCount() {
        return tryCount;
    }

    public boolean isSuccess(){
        return status == TaskStatus.SUCCESS;
    }
}
