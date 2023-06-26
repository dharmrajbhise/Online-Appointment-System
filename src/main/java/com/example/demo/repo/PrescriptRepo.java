package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Prescriptions;

@Repository
public interface PrescriptRepo extends JpaRepository<Prescriptions, Long> {

	List<Prescriptions> findByUsername(String username);

	
}
