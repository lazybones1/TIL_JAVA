package org.example.taskprocessor;

@FunctionalInterface
public interface Task {
    /**
     * 작업을 실행한다.
     * 실패 시 예외를 던질 수 있으며, RuntimeException이 아닌 경우도 포함한다.
     */
    void execute() throws Exception;
}
