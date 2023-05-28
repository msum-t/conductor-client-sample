package com.example.conductordemo.tasks;

import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
public class TaskService {

    public WorkflowTask salesTask(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setName("sales_review");
        workflowTask.setTaskReferenceName("sales_review_ref");
        workflowTask.setInputParameters( Map.of(
                "http_request", Map.of(
                        "uri", "http://localhost:8085/customer/salesReview",
                        "method", HttpMethod.PUT.toString()
                )
        ));
        workflowTask.setType("SIMPLE");
        workflowTask.setStartDelay(0);
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
    public WorkflowTask rmTask(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setName("rm_review");
        workflowTask.setTaskReferenceName("rm_review_ref");
        workflowTask.setInputParameters( Map.of(
                "http_request", Map.of(
                        "uri", "http://localhost:8085/customer/rmReview",
                        "method", HttpMethod.PUT.toString()
                )
        ));
        workflowTask.setType("SIMPLE");
        workflowTask.setStartDelay(0);
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
//        workflowTask.setInputParameters(Map.of());
//        workflowTask.setType("HUMAN");
//        workflowTask.setStartDelay(0);
//        workflowTask.setOptional(false);
//        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
    public WorkflowTask docTask(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setName("document_review");
        workflowTask.setTaskReferenceName("document_review_ref");
        workflowTask.setInputParameters( Map.of(
                "http_request", Map.of(
                        "uri", "http://localhost:8085/customer/docReview",
                        "method", HttpMethod.PUT.toString()
                )
        ));
        workflowTask.setType("SIMPLE");
        workflowTask.setStartDelay(0);
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
    public WorkflowTask sdcTask(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setName("sdc");
        workflowTask.setTaskReferenceName("sdc_ref");
        workflowTask.setInputParameters( Map.of(
                "http_request", Map.of(
                        "uri", "http://localhost:8085/customer/sdc",
                        "method", HttpMethod.PUT.toString()
                )
        ));
        workflowTask.setType("SIMPLE");
        workflowTask.setStartDelay(0);
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
//        workflowTask.setInputParameters(Map.of());
//        workflowTask.setType("SIMPLE");
//        workflowTask.setStartDelay(0);
//        workflowTask.setOptional(false);
//        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }
    public WorkflowTask welcome(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setName("welcome");
        workflowTask.setTaskReferenceName("welcome_ref");
        workflowTask.setInputParameters( Map.of(
                "http_request", Map.of(
                        "uri", "http://localhost:8085/customer/welcome",
                        "method", HttpMethod.PUT.toString()
                )
        ));
        workflowTask.setType("SIMPLE");
        workflowTask.setStartDelay(0);
        workflowTask.setOptional(false);
        workflowTask.setAsyncComplete(false);
        return workflowTask;
    }

}
