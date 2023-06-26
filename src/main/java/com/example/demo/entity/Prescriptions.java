package com.example.demo.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "prescriptions")
public class Prescriptions {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private String username;
	
	private String address;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	private String doctor;
	
	private String hospital;
	
	private String medicines;
	
	private String duration;
	
	private String instructions;
	
	public Prescriptions() {}

	public Prescriptions(String name, String username, String address, LocalDate date, String doctor, String hospital,
			String medicines, String duration, String instructions) {
		super();
		this.name = name;
		this.username = username;
		this.address = address;
		this.date = date;
		this.doctor = doctor;
		this.hospital = hospital;
		this.medicines = medicines;
		this.duration = duration;
		this.instructions = instructions;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getMedicines() {
		return medicines;
	}

	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@Override
	public String toString() {
		return "Prescriptions [id=" + id + ", name=" + name + ", username=" + username + ", address=" + address
				+ ", date=" + date + ", doctor=" + doctor + ", hospital=" + hospital + ", medicines=" + medicines
				+ ", duration=" + duration + ", instructions=" + instructions + "]";
	}

	
	

	
}
