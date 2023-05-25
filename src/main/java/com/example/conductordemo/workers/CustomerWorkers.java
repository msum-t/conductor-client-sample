package com.example.conductordemo.workers;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class CustomerWorkers implements Worker {
    private final String taskDefName;

    public CustomerWorkers(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {

        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);


        return result;
    }
}
