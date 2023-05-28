package com.example.conductordemo.controller;

import com.example.conductordemo.model.Customer;
import com.example.conductordemo.services.WorkflowService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private  final RestTemplate restTemplate;

    private final WebClient webclient;

    private final WorkflowService workflowService;

    private final ObjectMapper objectMapper;
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
        return Mono.just(Customer.builder().status("Sales review completed").build());
    }
    @PutMapping("/salesReview/{workflowId}")
    public Mono<Customer> updateSalesStatus(@RequestBody(required = false) Customer customer,@PathVariable String workflowId) {
        return getTaskId(workflowId)
                .flatMap(taskId -> webclient.post()
                        .uri("tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"))
                        .retrieve()
                        .toBodilessEntity())
                .thenReturn(Customer.builder().status("Sales review completed").build());
    }


    @PutMapping("/rmReview/{workflowId}/{taskId}")
    public Mono<Customer> updateRmStatus(@RequestBody(required = false) Customer customer, @PathVariable String workflowId, @PathVariable String taskId) {
        restTemplate.postForObject(rootUrl + "tasks", Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"), String.class);
        return  Mono.just(Customer.builder().status("rm review completed").build());
    }



    @PutMapping("/rmReview/{workflowId}")
    public Mono<Customer> updateRmStatus(@RequestBody(required = false) Customer customer, @PathVariable String workflowId) {
        return getTaskId(workflowId)
                .flatMap(taskId -> webclient.post()
                        .uri("tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"))
                        .retrieve()
                        .toBodilessEntity()
                        .thenReturn(Customer.builder().status("rm review completed").build()));
    }

    @PutMapping("/rmReview")
    public Mono<Customer> updateRmStatus(@RequestBody(required = false) Customer customer) {
        return Mono.just(Customer.builder().status("rm review completed").build());
    }


    @PutMapping("/docReview")
    public Mono<Customer> updateDocStatus(@RequestBody(required = false) Customer customer) {

        return Mono.just(Customer.builder().status("document review completed").build());
    }

    @PutMapping("/sdc")
    public Mono<Customer> updateSdcStatus(@RequestBody(required = false) Customer customer) {

        return Mono.just(Customer.builder().status("sdc completed").build());
    }
    @PutMapping("/sdc/{workflowId}")
    public Mono<Customer> sdcStatus(@RequestBody(required = false) Customer customer,@PathVariable String workflowId) {
        return getTaskId(workflowId)
                .flatMap(taskId -> webclient.post()
                        .uri( "tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"))
                        .retrieve()
                        .toBodilessEntity()
                        .thenReturn(Customer.builder().status("document review completed").build()));
    }

    @PutMapping("/docReview/{workflowId}")
    public Mono<Customer> updateDocStatus(@RequestBody(required = false) Customer customer,@PathVariable String workflowId) {
        return getTaskId(workflowId)
                .flatMap(taskId -> webclient.post()
                        .uri("tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"))
                        .retrieve()
                        .toBodilessEntity()
                        .thenReturn(Customer.builder().status("document review completed").build()));
    }


    @PutMapping("/welcome")
    public Mono<Customer> welcome(@RequestBody(required = false) Customer customer) {
        return Mono.just(Customer.builder().status("Customer onboarding journey completed successfully").build());
    }

    @PutMapping("/welcome/{workflowId}")
    public Mono<Customer> welcome(@RequestBody(required = false) Customer customer,@PathVariable String workflowId) {
        return getTaskId(workflowId)
                .flatMap(taskId -> webclient.post()
                        .uri("tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("workflowInstanceId", workflowId, "taskId", taskId, "status", "COMPLETED"))
                        .retrieve()
                        .toBodilessEntity()
                        .thenReturn(Customer.builder().status("Customer onboarding journey completed successfully").build()));    }


    private Mono<String> getTaskId(String workflowId) {
        return webclient
                .get()
                .uri("workflow/" + workflowId + "?includeTasks=true")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        JsonNode responseJson = objectMapper.readTree(responseBody);
                        JsonNode tasksArray = responseJson.get("tasks");
                        for (JsonNode task : tasksArray) {
                            String taskStatus = task.get("status").asText();
                            if (taskStatus.equals("IN_PROGRESS")) {
                                return Mono.just(task.get("taskId").asText());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Mono.empty();
                });
    }
}
