package com.example;

import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@EnableBinding(Sink.class)
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

@MessageEndpoint
class EmployeeProcessor {

	@Autowired
	private EmployeeRepository employeeRepository;

	@ServiceActivator(inputChannel = Sink.INPUT)
	public void acceptNewEmployee(String em) {
		this.employeeRepository.save(new Employee(em));
	}

}

@RefreshScope
@RestController
class MessageRestController {

	@Value("${message}")
	private String msg;

	@RequestMapping("/message")
	String message() {
		return this.msg;
	}
}

@RepositoryRestResource
interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@RestResource(path = "by-name")
	Collection<Employee> findByEmployeeName(@Param("rn") String rn);
}

@Entity
class Employee {

	@javax.persistence.Id
	@GeneratedValue()
	private Long Id;

	private String employeeName;

	public Employee(String name) {
		this.employeeName = name;
	}

	public Employee() {

	}

	@Override
	public String toString() {
		return "Employee [Id=" + Id + ", employeeName=" + employeeName + "]";
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}