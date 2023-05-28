package com.example.conductordemo.controller;

import com.example.conductordemo.model.Customer;
import com.example.conductordemo.services.WorkflowService;
import com.example.conductordemo.tasks.TaskClientWorkers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
@RequestMapping("/customer/workers")
@RestController
@RequiredArgsConstructor
public class CustomerWithWorkers {
    private  final RestTemplate restTemplate;

    private final WebClient webclient;

    private final WorkflowService workflowService;

    private final ObjectMapper objectMapper;

    private  final TaskClientWorkers taskClientWorkers;
    public static final String rootUrl = "http://localhost:8080/api/";

    @PostMapping("/save/{workflowName}")
    public Mono<Customer> customerDetails(@RequestBody Customer customer, @PathVariable String workflowName) {

        Flux.range(1,1000).subscribe();
        return webclient.post()
                .uri("workflow/" + workflowName + "?priority=0")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("additionalProp1", "{}", "additionalProp2", "{}", "additionalProp3", "{}"))
                .retrieve()
                .bodyToMono(String.class)
                .map(res -> {
                    customer.setCustomerId(res);
                    return customer;
                });
    }
    @PostMapping("/save")
    public Mono<Customer> customerDetails(@RequestBody Customer customer) {
        String workflowId = workflowService.startCustomerWorkflow(customer);
        customer.setCustomerId(workflowId);
        customer.setStatus("customer details received & onboarding started");
        return Mono.just(customer);
    }

    @PutMapping("/salesReview")
    public Mono<Customer> updateSalesStatus(@RequestBody(required = false) Customer customer) {
        TaskRunnerConfigurer salesReview = taskClientWorkers.taskRunnerConfigurer("sales_review");
        salesReview.getShutdownGracePeriodSeconds();

        return Mono.just(Customer.builder().status("Sales review completed").build());
    }


    @PutMapping("/rmReview")
    public Mono<Customer> updateRmStatus(@RequestBody(required = false) Customer customer) {
        TaskRunnerConfigurer salesReview = taskClientWorkers.taskRunnerConfigurer("rm_review");
        salesReview.getShutdownGracePeriodSeconds();
        Flux.range(1,1000).subscribe();
        return Mono.just(Customer.builder().status("rm review completed").build());
    }


    @PutMapping("/docReview")
    public Mono<Customer> updateDocStatus(@RequestBody(required = false) Customer customer) {
        TaskRunnerConfigurer salesReview = taskClientWorkers.taskRunnerConfigurer("document_review");
        salesReview.getShutdownGracePeriodSeconds();
        Flux.range(1,1000).subscribe();
        return Mono.just(Customer.builder().status("document review completed").build());
    }

    @PutMapping("/sdc")
    public Mono<Customer> updateSdcStatus(@RequestBody(required = false) Customer customer) {
        TaskRunnerConfigurer salesReview = taskClientWorkers.taskRunnerConfigurer("sdc");
        salesReview.getShutdownGracePeriodSeconds();
        Flux.range(1,1000).subscribe();
        return Mono.just(Customer.builder().status("sdc completed").build());
    }


    @PutMapping("/welcome")
    public Mono<Customer> welcome(@RequestBody(required = false) Customer customer) {
        TaskRunnerConfigurer salesReview = taskClientWorkers.taskRunnerConfigurer("welcome");
        salesReview.getShutdownGracePeriodSeconds();
        Flux.range(1,1000).subscribe();
        return Mono.just(Customer.builder().status("Customer onboarding journey completed successfully").build());
    }

}
