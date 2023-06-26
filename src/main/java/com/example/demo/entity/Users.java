package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String fullName;
	
	private String username;
	
	private String password;
	
	private String gender;
	
	private String email;
	
	private String bloodGroup;
	
	private String phone;
	
	private String birth_date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;
	
	public Users() {}

	public Users(String fullName, String username, String password, String gender, String email,String bloodGroup ,String phone,
			String birth_date, Role role) {
		super();
		this.fullName = fullName;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.email = email;
		this.bloodGroup = bloodGroup;
		this.phone = phone;
		this.birth_date = birth_date;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBlood_group(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", fullName=" + fullName + ", username=" + username + ", password=" + password
				+ ", gender=" + gender + ", email=" + email + ", bloodGroup=" + bloodGroup + ", phone=" + phone
				+ ", birth_date=" + birth_date + ", role=" + role + "]";
	}
	
}
