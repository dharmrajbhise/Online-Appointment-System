package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {

	Users findByUsername(String username);

	List<Users> findByBloodGroup(String bloodGroup);

	List<Users> findByEmail(String email);

	Users findByRole(String name);

	List<Users> findByRoleId(long id);

	Users findByFullName(String full_name);

	Users findByUsernameAndRole(String username, String string);

}
