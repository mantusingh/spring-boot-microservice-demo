package com.microservice.demo.rest.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.demo.vo.Employee;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping("/employee")
public class EmployeeApiGatewayRestController {

	@Autowired
	private RestTemplate restTemplate;

	public Collection<String> getEmployeeNamesFallback() {
		return new ArrayList<>();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private Source source;

	@RequestMapping(method = RequestMethod.POST)
	public void writeEmployee(@RequestBody Employee employee) {
		Message<String> message = MessageBuilder.withPayload(employee.getEmployeeName()).build();
		this.source.output().send(message);

	}

	@HystrixCommand(fallbackMethod = "getEmployeeNamesFallback")
	@RequestMapping(method = RequestMethod.GET, value = "/names")
	Collection<String> getEmployeeNames() {

		ParameterizedTypeReference<Resources<Employee>> ptr = new ParameterizedTypeReference<Resources<Employee>>() {
		};

		ResponseEntity<Resources<Employee>> entity = this.restTemplate
				.exchange("http://localhost:9999/employee-service/employees", HttpMethod.GET, null, ptr);
		return entity.getBody().getContent().stream().map(Employee::getEmployeeName).collect(Collectors.toList());
	}

}