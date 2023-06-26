package com.example.demo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.example.demo.repo.AppointRepo;
import com.example.demo.repo.CertifyRepo;
import com.example.demo.repo.MessagesRepo;
import com.example.demo.repo.PrescriptRepo;
import com.example.demo.repo.ReceiptRepo;
import com.example.demo.repo.ReferralRepo;
import com.example.demo.repo.ReportRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UsersRepo;

@Service
public class UsersDao implements usersService {
	
	@Autowired
	private ReferralRepo repo9;
	
	@Autowired
	private ReceiptRepo repo8;
	
	@Autowired
	private ReportRepo repo7;
	
	@Autowired
	private CertifyRepo repo6;
	
	@Autowired
	private PrescriptRepo repo5;
	
	@Autowired
	private AppointRepo repo4;
	
	@Autowired
	private MessagesRepo repo3;
	
	@Autowired
	private RoleRepo repo2;
	
	@Autowired
	private UsersRepo repo;

	@Override
	public Users saveUser(Users user) {
		
		return repo.save(user);
	}

	@Override
	public Role saveRole(Users user) {
		
		return repo2.save(user.getRole()) ;
	}

	@Override
	public Messages saveMessage(Messages message) {
		
		return repo3.save(message);
	}

	@Override
	public List<Messages> getAll() {
		
		return repo3.findAll();
	}

	@Override
	public Users getByUsername(String username) {
		
		return repo.findByUsername(username);
	}

	@Override
	public Appoinments saveAppoinment(Appoinments appoint) {
		
		return repo4.save(appoint);
	}

	@Override
	public List<Appoinments> getByName(String name) {
		
		return repo4.findByName(name);
	}

	@Override
	public List<Appoinments> getall() {
		
		return repo4.findAll();
	}

	@Override
	public List<Appoinments> getByDate(String date) {
		
		return repo4.findByDate(date);
	}

	@Override
	public List<Users> getAllUsers() {
		
		return repo.findAll();
	}

	@Override
	public List<Users> getByEmail(String email) {
		
		return repo.findByEmail(email);
	}

	@Override
	public List<Users> getByBlood(String bloodGroup) {
		
		return repo.findByBloodGroup(bloodGroup);
	}

	@Override
	public List<Prescriptions> getAllPrescript() {
		
		return repo5.findAll();
	}

	@Override
	public Prescriptions savePriscriptions(Prescriptions prescript) {
		
		return repo5.save(prescript);
	}

	@Override
	public Certificates saveCertificate(Certificates certify) {
		
		return repo6.save(certify);
	}

	@Override
	public List<Certificates> getAllCertificates() {
		
		return repo6.findAll();
	}

	@Override
	public Certificates getcertById(long id) {
		
		return repo6.findById(id).orElse(null);
	}

	@Override
	public Prescriptions getById(long id) {
		
		return repo5.findById(id).orElse(null);
	}

	@Override
	public List<Role> getByname(String name) {
		
		return repo2.findByName(name);
	}

	@Override
	public List<Users> findByRoleId(Long id) {
		
		return repo.findByRoleId(id);
	}

	@Override
	public Users getByFullName(String fullName) {
		
		return repo.findByFullName(fullName);
	}

	@Override
	public List<lab_report> getAllReports() {
		
		return repo7.findAll();
	}

	@Override
	public lab_report saveReport(MultipartFile file,String username,String type) throws IOException {
		
		String fileName = file.getOriginalFilename();
		byte[] files = file.getBytes();
        lab_report labReport = new lab_report();
			labReport.setFile(files);
			labReport.setUsername(username);
			labReport.setType(type);
			labReport.setFileName(fileName);
			return repo7.save(labReport);
			
		
	}

	@Override
	public lab_report getReportById(long id) {
		
		return repo7.findById(id).orElse(null);
	}

	@Override
	public lab_report loadReport(String fileName) throws FileNotFoundException {
		
		lab_report labReport = repo7.findByFileName(fileName);
        if (labReport == null) {
            throw new FileNotFoundException("Lab report not found: " + fileName);
        }

        // Load the file data from the lab report entity
        byte[] fileData = labReport.getFile();

        // Save the file data to a temporary file
        String tempFileName = "temp_" + fileName;
        Path tempFilePath = Paths.get(tempFileName);
        try {
			Files.write(tempFilePath, fileData);
			// Read the file content
			byte[] content = Files.readAllBytes(tempFilePath);
			
			// Delete the temporary file
			Files.delete(tempFilePath);
			labReport.setFile(content);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        // Update the lab report entity with the actual file content

        return labReport;
		
		
	}

	@Override
	public List<Receipt> getAllReceipts() {
		
		return repo8.findAll();
	}

	@Override
	public Receipt saveReceipt(Receipt receipt) {
		
		return repo8.save(receipt);
	}

	@Override
	public Receipt getByReceiptId(long id) {
		
		return repo8.findById(id).orElse(null);
	}

	@Override
	public List<Referral> getAllReferrals() {
		
		return repo9.findAll();
	}

	@Override
	public Referral saveReferral(Referral refer) {
		
		return repo9.save(refer);
	}

	@Override
	public Referral getReferById(long id) {
		
		return repo9.findById(id).orElse(null);
	}

	@Override
	public List<Certificates> getCertByUsername(String username) {

		return repo6.findByUsername(username);
	}

	@Override
	public List<Prescriptions> getPresByUsername(String username) {
		
		return repo5.findByUsername(username);
	}

	@Override
	public List<lab_report> getReportByUsername(String username) {
		
		return repo7.findByUsername(username);
	}

	@Override
	public List<Receipt> getReceiptByUsername(String username) {
		
		return repo8.findByUsername(username);
	}

	@Override
	public List<Referral> getReferralByUsername(String username) {
		
		return repo9.findByUsername(username);
	}

	@Override
	public List<Appoinments> getByDoctor(String doctor) {
		
		return repo4.findByDoctor(doctor);
	}

}
