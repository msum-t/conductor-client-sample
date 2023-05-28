package com.example.conductordemo.tasks;

import com.example.conductordemo.workers.CustomerWorkers;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.worker.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskClientWorkers {

    private final TaskClient taskClient;


    public TaskRunnerConfigurer taskRunnerConfigurer(String taskDef){
        //taskClient.setRootURI("http://13.250.191.22:8080/api/");
        taskClient.setRootURI("http://localhost:8080/api/");

        int threadCount = 1;

        Worker worker1 = new CustomerWorkers(taskDef);
        TaskRunnerConfigurer configurer = new TaskRunnerConfigurer.Builder(taskClient, List.of(worker1))
                .withThreadCount(threadCount)
                .build();



        // Start the polling and execution of tasks
        configurer.init();
        return configurer;
    }
}
