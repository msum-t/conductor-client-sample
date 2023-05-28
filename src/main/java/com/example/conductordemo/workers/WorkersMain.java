package com.example.conductordemo.workers;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

import java.util.Arrays;
import java.util.Map;

public class WorkersMain {
    public static void main(String[] args) {

        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI("http://localhost:8080/api/");

        int threadCount = 5;

//        Worker worker1 = new CustomerWorkers("sales_review");
//        Worker worker2 = new CustomerWorkers("document_review");
        Worker worker1 = new CustomerWorkers("sdc");
        Worker worker2 = new CustomerWorkers("sales_review");
        Worker worker3 = new CustomerWorkers("ops_team_approval");
       // Worker worker4 = new CustomerWorkers("task_2");

        // Create TaskRunnerConfigurer
        TaskRunnerConfigurer configurer = new TaskRunnerConfigurer.Builder(taskClient, Arrays.asList(worker1,worker2,worker3))
                .withThreadCount(threadCount)
                        .build();


        // Start the polling and execution of tasks
        configurer.init();
    }
}
