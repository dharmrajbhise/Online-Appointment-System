package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Appoinments;
import com.example.demo.entity.Certificates;
import com.example.demo.entity.lab_report;
import com.example.demo.entity.Messages;
import com.example.demo.entity.Prescriptions;
import com.example.demo.entity.Receipt;
import com.example.demo.entity.Referral;
import com.example.demo.entity.Role;
import com.example.demo.entity.Users;

public interface usersService {

	Users saveUser(Users user);
	
	List<Users> getAllUsers();
	
	List<Users> getByEmail(String email);
	
	List<Users> getByBlood(String bloodGroup);

	Role saveRole(Users user);
	
	Messages saveMessage(Messages message);
	
	List<Messages> getAll();
	
	Users getByUsername(String username);
	
	Users getByFullName(String fullName);
	
	Appoinments saveAppoinment(Appoinments appoint);
	
	List<Appoinments> getByName(String name );
	
	List<Appoinments> getall();
	
	List<Appoinments> getByDate(String date);
	
	List<Prescriptions> getAllPrescript();
	
	Prescriptions savePriscriptions(Prescriptions prescript);
	
	Certificates saveCertificate(Certificates certify);
	
	List<Certificates> getAllCertificates();
	
	Certificates getcertById(long id);
	
	Prescriptions getById(long id);
	
	List<Role> getByname(String name);
	
	List<Users> findByRoleId(Long id);
	
	List<lab_report> getAllReports();
	
	lab_report saveReport(MultipartFile file,String username,String type) throws IOException;
	
	lab_report getReportById(long id);
	
	lab_report loadReport(String fileName) throws FileNotFoundException;
	
	List<Receipt> getAllReceipts();
	
	Receipt saveReceipt(Receipt receipt);
	
	Receipt getByReceiptId(long id);
	
	List<Referral> getAllReferrals();
	
	Referral saveReferral(Referral refer);
	
	Referral getReferById(long id);
	
	List<Certificates> getCertByUsername(String username);
	
	List<Prescriptions> getPresByUsername(String username);
	
	List<lab_report> getReportByUsername(String username);
	
	List<Receipt> getReceiptByUsername(String username);
	
	List<Referral> getReferralByUsername(String username);
	
	List<Appoinments> getByDoctor(String doctor);
	

}
