package com.microservice.demo.message.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.microservice.demo.domain.Employee;
import com.microservice.demo.rest.repo.EmployeeRepository;

@MessageEndpoint
public class EmployeeProcessor {

	@Autowired
	private EmployeeRepository employeeRepository;

	@ServiceActivator(inputChannel = Sink.INPUT)
	public void acceptNewEmployee(String em) {
		this.employeeRepository.save(new Employee(em));
	}

}
