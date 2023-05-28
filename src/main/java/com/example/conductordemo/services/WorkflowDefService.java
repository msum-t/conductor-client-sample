package com.example.conductordemo.services;

import com.example.conductordemo.tasks.TaskService;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WorkflowDefService {

    private final TaskService taskService;

    public WorkflowDef customerOnboardingDef(String customerName){
        WorkflowDef workflowDef=new WorkflowDef();
        workflowDef.setName(customerName);
        workflowDef.setDescription("This Customer onboarding dynamic workflow");
        workflowDef.setVersion(1);
        workflowDef.setTasks(List.of(taskService.salesTask(),taskService.rmTask()
                ,taskService.docTask(),taskService.sdcTask(),taskService.welcome()));
        workflowDef.setInputParameters(List.of(""));
        workflowDef.setOutputParameters(Map.of("msg","${welcome_ref.output.response.body.status}"));
        workflowDef.setRestartable(true);
        workflowDef.setSchemaVersion(2);
        workflowDef.setWorkflowStatusListenerEnabled(false);
        workflowDef.setOwnerEmail("admin@gmail.com");
        workflowDef.setTimeoutPolicy(WorkflowDef.TimeoutPolicy.ALERT_ONLY);
        workflowDef.setTimeoutSeconds(0);
        
        return workflowDef;
    }



}
