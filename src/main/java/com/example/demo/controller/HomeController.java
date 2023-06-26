package com.example.demo.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Appoinments;
import com.example.demo.entity.Messages;
import com.example.demo.entity.Role;
import com.example.demo.entity.Users;
import com.example.demo.repo.RoleRepo;
import com.example.demo.service.usersService;

@Controller
public class HomeController {
	
	@Autowired
	private RoleRepo repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private usersService us;
	
	@GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String secured(Principal principal, Model model,Role role) {
    	
    	String username = principal.getName();
    	
    	Users user = us.getByUsername(username);
    	if (user != null) {
    	    System.out.println(user.getFullName());
    	} else {
    	    System.out.println("User is null");
    	}
    	model.addAttribute("user", user);
//    	System.out.println(user.getFullName());
    	
    	model.addAttribute("username", username);
    	
    	List<Role> user2 = us.getByname("DOCTOR");
    	
    	List<Users> ids = new ArrayList<>();
    	 for(Role u1 : user2) {
    		 	
    		 long id = u1.getId();
    		 List<Users> user3 = us.findByRoleId(id);
    		 ids.addAll(user3);
    	}	 
    	 
    	 model.addAttribute("usern", ids);
    	 
    	
        return "user";
    }
    
    @GetMapping("/about")
    public String about() {
    	return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
    	return "contact";
    }
    
    @PostMapping("/contact-message")
    public String message(@ModelAttribute Messages message,Model model) {
    	
    	us.saveMessage(message);
    	
    	model.addAttribute("message","Message sent successfully!");
    	
    	return "index";
    }
    
    
    @GetMapping("/login")
    public String login(HttpServletRequest request,@RequestParam(value = "error", required = false) String error, Model model) {

    	if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
    	
    	 String url = request.getRequestURI();
         if (url.contains("/admin") && !url.contains("/user")) {
             return "redirect:/error";
         }
    	
    	return "login";
    }
    
    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }
    
    
    @GetMapping("/register")
    public String register() {
    	return "register";
    }
    
    @PostMapping("/Registration")
    public String registration(@ModelAttribute Users user,Model model) {
    	
    	Role role = repo.save(user.getRole());
    	user.setRole(role);
    	
    	String password = passwordEncoder.encode(user.getPassword());
    	user.setPassword(password);
    	
    	us.saveUser(user);
    	
    	model.addAttribute("message","User Registered Successfully!");
    	
    	
    	return "index";
    }
    
    @RequestMapping("/admin")
	public String admin(Model model,Principal principal) {
    	
    	String username = principal.getName();
    	
    	Users user = us.getByUsername(username);
    	
    	String doctor = user.getFullName();
    	
    	List<Appoinments> appoint = us.getByDoctor(doctor);
    	
    	model.addAttribute("appoint", appoint);
		
		return "admin";
	}
    
    @GetMapping("/patient/login")
    public String showPatientLoginForm() {
        return "PatientLogin";
    }

	
	
}
