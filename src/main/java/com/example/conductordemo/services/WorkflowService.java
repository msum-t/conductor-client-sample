package com.example.conductordemo.services;

import com.example.conductordemo.model.Customer;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class WorkflowService {

    private final WorkflowClient workflowClient;

    private final MetadataClient metadataClient;
    private final WorkflowDefService workflowDefService;


    private final  StartWorkflowRequest startWorkflowRequest ;

    public String startCustomerWorkflow(Customer customerDetails) {
        String uuid = " _ " + UUID.randomUUID().toString();
        String workflowName=customerDetails.getCustomerName()+ "_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss.SSSSSSSS")) ;
        //String rootUrl = "http://13.250.191.22:8080/api/";
       String rootUrl = "http://localhost:8080/api/";

        workflowClient.setRootURI(rootUrl);
        startWorkflowRequest.setName(workflowName);
        startWorkflowRequest.setWorkflowDef(workflowDefService.customerOnboardingDef(workflowName));
        String workflowId = workflowClient.startWorkflow(startWorkflowRequest);
        metadataClient.setRootURI(rootUrl);
        metadataClient.registerWorkflowDef(startWorkflowRequest.getWorkflowDef());
        log.info("Workflow id: {}", workflowId);
        return workflowId;
    }


    public static String generateUniqueFileName() {
        String filename = "";
        long millis = System.currentTimeMillis();
        String datetime = new Date().toGMTString();
        datetime = datetime.replace(" ", "");
        datetime = datetime.replace(":", "");
        String rndchars = RandomStringUtils.randomAlphanumeric(64);
        filename = rndchars + "_" + datetime + "_" + millis;
        return filename;
    }

}
