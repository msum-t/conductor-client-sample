package com.example.conductordemo;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class ConductorDemoApplication {

	public static void main(String[] args) {
		//BlockHound.install();
		SpringApplication.run(ConductorDemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return   new RestTemplateBuilder()
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
@Bean
	public WebClient webClient(){
	return WebClient.builder()
			//.baseUrl("http://13.250.191.22:8080/api/")
			.baseUrl("http://localhost:8080/api/")
			.defaultCookie("cookie-name", "cookie-value")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

}


	@Bean
	public WorkflowClient workflowClient(){
		return new WorkflowClient();
	}

	@Bean
	public MetadataClient metadataClient(){
		return new MetadataClient();
	}
	@Bean
	public StartWorkflowRequest startWorkflowRequest(){
		return new StartWorkflowRequest();
	}
	@Bean
	public TaskClient taskClient(){
		return  new TaskClient();
	}

}
