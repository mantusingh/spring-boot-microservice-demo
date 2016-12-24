package com.microservice.demo.main;

import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.microservice.demo.domain.Employee;
import com.microservice.demo.rest.repo.EmployeeRepository;

@EnableDiscoveryClient
@EnableBinding(Sink.class)
@ComponentScan(basePackages = "com.microservice.*")
@EnableJpaRepositories("com.microservice.*")
@org.springframework.boot.autoconfigure.domain.EntityScan("com.microservice.*")
@SpringBootApplication
public class EmployeeServiceApplication {

	@Bean
	CommandLineRunner dummyCLR(EmployeeRepository employeeRepository) {
		return args -> {
			Stream.of("Dave", "George", "Rod", "Mattias").forEach(name -> employeeRepository.save(new Employee(name)));
			// employeeRepository.findAll().forEach(System.out::println);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}
}