package com.microservice.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
public class Employee {

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