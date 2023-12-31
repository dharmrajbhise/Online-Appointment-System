package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.example.demo.service.usersService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private usersService us;
	
	@GetMapping("/messages")
	public String messages(Model model) {
		
		List<Messages> messages = us.getAll();
		
		model.addAttribute("messages", messages);
		
		return "messages";
	}
	
	@GetMapping("/byDate")
	public String Datefilter(@RequestParam("date") String date,Model model) {
		
		List<Appoinments> appoint = us.getByDate(date);
		
		if(appoint !=null) {
			
			 model.addAttribute("appoint", appoint);
		}
		
		if(appoint.isEmpty()) {
			System.out.println(appoint);
			model.addAttribute("nope", "No Appointments on This Day");
		}
		
		return "admin";
	}
	
	@GetMapping("/all")
	public String AllUsers(Model model) {
		
		List<Users> users = us.getAllUsers();
		model.addAttribute("users", users);
		
		return "AllUsers";
	}
	
	@GetMapping("/byblood")
	public String bloodFilter(@RequestParam("bloodGroup") String bloodGroup,Model model) {
		
		List<Users> users = us.getByBlood(bloodGroup);
		if(users != null) {
		model.addAttribute("users", users);
		}
		if(users.isEmpty()) {
			model.addAttribute("nope", "There is no User of this blood group");
		}
			
		return "AllUsers";
	}
	
	@GetMapping("/byUsername")
	public String userFilter(@RequestParam("email") String email,Model model) {
		
//		List<Users> user = us.getAllUsers();
//		System.out.println(user);
//		
//		for(Users u1:user) {
//			
//			String emailum = u1.getEmail();
//			List<Users> users = us.getByEmail(emailum);
//			System.out.println(users);
//		}
		
		List<Users> user = us.getByEmail(email);
		
		System.out.println(email);
		if(user != null) {
		model.addAttribute("users", user);
		}
		if(user.isEmpty()) {
			model.addAttribute("nope","There is no User of this username");
		}
		
		return "AllUsers";
	}
	
	@GetMapping("/all/{username}")
	public String profile(@PathVariable("username") String username,Model model) {
		
		Users user = us.getByUsername(username);
		model.addAttribute("user", user);
		
		return "AdminProfile";
	}
	
	@RequestMapping("/all/prescriptions/{username}")
	public String prescript(Model model,@PathVariable("username") String username,Principal principal ) {
		
		List<Prescriptions> prescript = us.getAllPrescript();
		model.addAttribute("prescript", prescript);
		
		Users user = us.getByUsername(username);
		model.addAttribute("user", user);
		
		String user1 = principal.getName();
		Users user2 = us.getByUsername(user1);
		String full = user2.getFullName();
		model.addAttribute("full", full);
		System.out.println(full);
		
		return "Prescript";
	}
	
	@PostMapping("/all/prescriptions/add/{username}")
	public String Savepriscript(@ModelAttribute("prescript") Prescriptions prescript,Model model,@PathVariable("username") String username) {
		
		us.savePriscriptions(prescript);
		
		List<Prescriptions> prescriptions = us.getAllPrescript();
		model.addAttribute("prescript", prescriptions);
		
		Users user = us.getByUsername(username);
		model.addAttribute("user", user);
		
		model.addAttribute("message", "Prescription Added Successfully!");
		
		
		return "Prescript";
	}
	
	@GetMapping("/all/certificates/{username}")
	public String certify(@PathVariable("username") String username,Model model) {

		List<Certificates> certify = us.getAllCertificates();
		model.addAttribute("certify", certify);
		
		Users user = us.getByUsername(username);
		model.addAttribute("user", user);
		
		return "certificates";
	}
	
	@PostMapping("/all/certificates/add/{username}")
	public String saveCertify(@ModelAttribute("certify") Certificates certify,Model model,@PathVariable("username") String username) {
		
		us.saveCertificate(certify);
		
		List<Certificates> certifi = us.getAllCertificates();
		model.addAttribute("certify", certifi);
		
		Users user = us.getByUsername(username);
		model.addAttribute("user", user);
		
		model.addAttribute("message", "Certificate Issued Successfully!");
		
		return "certificates";
	}
	
	@RequestMapping("/all/cerificates/download/{id}")
	@ResponseBody
	public void download(HttpServletResponse response,@PathVariable("id") long id) throws IOException {
		
		Certificates certificate = us.getcertById(id);
		
		PDDocument document = new PDDocument();
		
		PDPage page = new PDPage();
		document.addPage(page);
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		if(certificate.getCertificateName().equals("Medical Certificate")) {
		contentStream.beginText();
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
		contentStream.newLineAtOffset(50, 700);
        contentStream.showText(certificate.getCertificateName());
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.setFont(PDType1Font.HELVETICA, 15);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("This is to certify that the individual known are "+certificate.getPatientName()+" ,has ");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("undergone a medical examination, on "+certificate.getIssueDate());
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("and he is currently suffering from a _______________.");
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("The examiner has advised that for the sake of the individuals ");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("overall health, he should be relieved ");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("of his duties for duration of _________________.");
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("The patient is advised to refrain from engaging in strenuous activities");
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("until fully recovered.");
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Hospital Seal and Signature");
        contentStream.endText();
        contentStream.close();

		}
		
		if(certificate.getCertificateName().equals("Birth Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that the following child was born:");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Name: ______________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Date of Birth: ___________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Place of Birth: ___________________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Gender: _____________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Father's Name: ________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Mother's Name: ____________");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();

			
		}
		
		if(certificate.getCertificateName().equals("Medical Fitness Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that "+certificate.getPatientName()+" has undergone a medical examination");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("and is found to be medically fit with the following details:");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Date of Examination: "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Height: _________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Weight: ____________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Blood Pressure: _______________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Heart Rate: __________________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("Respiratory Rate: ___________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("The patient is deemed medically fit for the specified purpose.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
			
		}
		
		if(certificate.getCertificateName().equals("Discharge Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that "+certificate.getPatientName()+" has been discharged from");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("___________________ on ______________ after successful treatment");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("of _____________________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The patient's condition has significantly improved, and they are");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("deemed fit for discharge. The patient is advised to follow the");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("prescribed medication and attend follow-up appointments as advised.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
		}
		
		if(certificate.getCertificateName().equals("Death Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that "+certificate.getPatientName()+" passed away on ___________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The cause of death is reported as ____________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The deceased was _____ years old at the time of death and");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("was born on _________. The deceased's gender was _________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This certificate is issued based on the available records");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("and in accordance with applicable regulations.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
		}
		
		if(certificate.getCertificateName().equals("Surgery Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that "+certificate.getPatientName()+" underwent surgery on _________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The surgical procedure performed was _____________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The surgery was performed by ____________ at ___________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The patient's condition and progress were monitored closely,");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("and appropriate care and treatment were provided.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("The surgery was successful, and the patient is currently");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("undergoing post-operative care.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
		}
		
		if(certificate.getCertificateName().equals("Immunization Certificate")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("This is to certify that "+certificate.getPatientName()+" has received the following ");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("immunizations:");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("- Vaccine 1 on _____________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("- Vaccine 2 on _______________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("- Vaccine 3 on _________________");
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.newLineAtOffset(0, -15);
	        contentStream.showText("The immunizations were administered by _____________________");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("at _____________________________.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
			
		}
		
		if(certificate.getCertificateName().equals("Organ Donor Card")) {
			
			contentStream.beginText();
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
	        contentStream.newLineAtOffset(50, 700);
	        contentStream.showText(certificate.getCertificateName());
	        contentStream.setFont(PDType1Font.HELVETICA, 15);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("I, "+certificate.getPatientName()+", hereby declare my wish to be an organ donor.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("I authorize the donation of any of my organs or tissues,");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("in accordance with applicable laws and regulations,");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("to be used for transplantation purposes,");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("with the intention of saving or enhancing the lives of others.");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Signed: _____________________________");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Date: _______________________________");
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Issue Date : "+certificate.getIssueDate());
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.newLineAtOffset(0, -20);
	        contentStream.showText("Hospital Seal and Signature");
	        contentStream.endText();
	        contentStream.close();
			
		}
		
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    document.save(byteArrayOutputStream);
	    document.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=certificate.pdf");

	    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
	    response.getOutputStream().flush();
		
	}
	
	@GetMapping("/all/prescriptions/download/{id}")
	@ResponseBody
	public void preDownload(HttpServletResponse response,@PathVariable("id") long id) throws IOException {
		
		Prescriptions prescript = us.getById(id);
		String address = prescript.getAddress().replace(" ", ",");
		String[] add = address.split(",");
		
		String instruct = prescript.getInstructions();
		String[] indata = instruct.split(",");
		
		
		String data = prescript.getMedicines();
		String[] parts = data.split(",");

		System.out.println(prescript);
		
		PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Prescription");
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText(prescript.getHospital());
        InputStream inputStream = getClass().getResourceAsStream("/font/Inconsolata-Regular.ttf");
        PDType0Font font = PDType0Font.load(document, inputStream);
        contentStream.setFont(font, 18);
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Patient Name: "+prescript.getName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Doctor Name : "+prescript.getDoctor());
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Address: ");
        for (String part : add) {
 		   String medicine = part;
 		   contentStream.showText(medicine);
 		   contentStream.newLineAtOffset(0, -15);
 		}
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Date: "+prescript.getDate());
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Medicines: ");
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        for (String part : parts) {
		   String medicine = part;
		   contentStream.showText("tab. "+medicine);
		   contentStream.newLineAtOffset(0, -15);
		}
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Duration in days: "+prescript.getDuration().replace(".", "\n"));
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Instructions: ");
        for (String part : indata) {
 		   String medicine = part;
 		   contentStream.showText("* "+medicine);
 		   contentStream.newLineAtOffset(0, -15);
 		}
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Hospital Seal and Signature");
        contentStream.endText();
        contentStream.close();

        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    document.save(byteArrayOutputStream);
	    document.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=prescript.pdf");

	    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
	    response.getOutputStream().flush();
		
	}
	
	@GetMapping("/all/labReports/{username}")
	public String labReport(Model model,@PathVariable("username") String username) {
		
		model.addAttribute("username", username);
		
		List<lab_report> report = us.getAllReports();
		
		model.addAttribute("report", report);
		
		return "reports";
	}
	
	@PostMapping("/all/labReports/add{username}")
	public String addReport(Model model, @PathVariable("username") String username ,@RequestParam("file") MultipartFile file,@RequestParam("type") String type) throws IOException {
               
            us.saveReport(file,username,type);
            model.addAttribute("message", "Lab report Added Successfully!");
		
            model.addAttribute("username", username);
    		
    		List<lab_report> report = us.getAllReports();
    		
    		model.addAttribute("report", report);
//		us.saveReport(report);
		
		return "reports";
	}
	
	@GetMapping("/all/reports/download{id}")
    public ResponseEntity<byte[]> downloadLabReport(@PathVariable Long id) throws FileNotFoundException {
        lab_report labReport = us.getReportById(id);
        
        byte[] fileData = labReport.getFile();
        String fileName = labReport.getFileName();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        return  ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }
    
    @GetMapping("/all/receipts/{username}")
    public String receipts(Model model,@PathVariable("username") String username) {
    	
    	Users user = us.getByUsername(username);
    	model.addAttribute("user", user);
    	
    	List<Role> user2 = us.getByname("DOCTOR");
    	
    	List<Users> ids = new ArrayList<>();
    	 for(Role u1 : user2) {
    		 	
    		 long id = u1.getId();
    		 List<Users> user3 = us.findByRoleId(id);
    		 ids.addAll(user3);
    	}	 
    	 
    	 model.addAttribute("usern", ids);
    	
    	List<Receipt> receipt = us.getAllReceipts();
    	model.addAttribute("receipt", receipt);
    	
    	return "receipts";
    }
    
    @PostMapping("/all/receipts/add{username}")
    public String addreceipt(@ModelAttribute("receipt") Receipt receipt,Model model,@PathVariable("username") String username) {
    	
    	
    	us.saveReceipt(receipt);
    	
    	Users user = us.getByUsername(username);
    	model.addAttribute("user", user);
    	
    	List<Role> user2 = us.getByname("DOCTOR");
    	
    	List<Users> ids = new ArrayList<>();
    	 for(Role u1 : user2) {
    		 	
    		 long id = u1.getId();
    		 List<Users> user3 = us.findByRoleId(id);
    		 ids.addAll(user3);
    	}	 
    	 
    	 model.addAttribute("usern", ids);
    	
    	
    	List<Receipt> receiptum = us.getAllReceipts();
    	model.addAttribute("receipt", receiptum);
    	
    	model.addAttribute("message", "Receipt Added Successfully!");
    	
    	return "receipts";
    }
    
    @GetMapping("/all/receipts/download{id}")
    @ResponseBody
    public void downloadReceipt(@PathVariable("id") long id,HttpServletResponse response) throws IOException {
    	
    	Receipt receipt = us.getByReceiptId(id);
    	String service = receipt.getServices();
    	String[] serv = service.split(",");
    	
    	String add = receipt.getAddress();
    	String[] receip = add.split(",");
    	
    	
    	PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText(receipt.getHospital());
        InputStream inputStream = getClass().getResourceAsStream("/font/Inconsolata-Regular.ttf");
        PDType0Font font = PDType0Font.load(document, inputStream);
        contentStream.setFont(font, 18);
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Receipt");
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Patient Name: "+receipt.getName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Address: ");
        
        for(String s : receip) {
        	contentStream.showText(s);
        	contentStream.newLineAtOffset(0, -15);
        }
        
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Doctor : "+receipt.getDoctor());
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Date : "+receipt.getDate());
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Services : ");
        contentStream.newLineAtOffset(0, -20);
        contentStream.newLineAtOffset(0, -20);
        for(String s : serv) {
        	contentStream.showText(s);
        	contentStream.newLineAtOffset(0, -20);
        	contentStream.newLineAtOffset(0, -20);
        }
        
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("CGST : _________");
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("SGST : ___________");
        contentStream.newLineAtOffset(0, -25);
        contentStream.newLineAtOffset(0, -25);
        contentStream.showText("Total : ______________");
        contentStream.newLineAtOffset(0, -15);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hospital Seal And Signature");
        contentStream.endText();
        contentStream.close();

        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    document.save(byteArrayOutputStream);
	    document.close();

	    response.setContentType("application/pdf");
	    response.setHeader("Content-Disposition", "attachment; filename=Receipt.pdf");

	    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
	    response.getOutputStream().flush();
    	
    }
    
    @GetMapping("/all/referral/{username}")
    public String refer(@PathVariable("username") String username,Model model,Principal principal) {
    	
    	Users user = us.getByUsername(username);
    	model.addAttribute("user", user);
    	
    	String doctor = principal.getName();
    	Users doctorUser = us.getByUsername(doctor);
    	String doctorName = doctorUser.getFullName();
    	model.addAttribute("full", doctorName);
    	
    	List<Referral> refer = us.getAllReferrals();
    	model.addAttribute("refer", refer);
    	
    	return "referral";
    }
    
    @PostMapping("/all/referral/add/{username}")
    public String addReferral(@ModelAttribute("refer") Referral refer,Model model,@PathVariable("username") String username,Principal principal) {
    	
    	us.saveReferral(refer);
    	
    	Users user = us.getByUsername(username);
    	model.addAttribute("user", user);
    	
    	String doctor = principal.getName();
    	Users doctorUser = us.getByUsername(doctor);
    	String doctorName = doctorUser.getFullName();
    	model.addAttribute("full", doctorName);
    	
    	List<Referral> refert = us.getAllReferrals();
    	model.addAttribute("refer", refert);
    	
    	model.addAttribute("message", "Refer Note Added Successfully!");
    	
    	return "referral";
    }
    
    @GetMapping("/all/referral/download/{id}")
    @ResponseBody
    public void downloadNote(@PathVariable("id") long id,HttpServletResponse response,Principal principal) throws IOException {
    	
    	String username = principal.getName();
    	Users user = us.getByUsername(username);
    	
    	Referral refer = us.getReferById(id);
    	
    	String patient = refer.getUsername();
    	
    	Users patientUser = us.getByUsername(patient);
    	
    	 PDDocument document = new PDDocument();

         // Create a new page
         PDPage page = new PDPage(PDRectangle.A4);
         document.addPage(page);

         // Create a new content stream for the page
         PDPageContentStream contentStream = new PDPageContentStream(document, page);

         // Set font and font size for the document
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

         // Define the document dimensions and positions
         float margin = 50;
         float yStart = page.getMediaBox().getHeight() - margin;
         float yPosition = yStart;

         // Title
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
         contentStream.showText("Hospital Referral Note");
         contentStream.endText();
         yPosition -= 30;

         // Referral Details
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText(refer.getDoctor());
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText(refer.getHospital());
         contentStream.endText();
         yPosition -= 20;

         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("contact : "+user.getPhone());
         contentStream.endText();
         yPosition -= 20;
         yPosition -= 20;
         yPosition -= 20;

         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Patient Information : ");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Name : "+refer.getPatientName());
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("contact : "+patientUser.getPhone());
         contentStream.endText();
         yPosition -= 20;
         yPosition -= 20;

         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Date: " + refer.getDate());
         contentStream.endText();
         yPosition -= 30;
         yPosition -= 30;
         
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Subject: " + refer.getSubject());
         contentStream.endText();
         yPosition -= 30;

         // Reason for Referral
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Dear "+refer.getRecipientName());
         contentStream.endText();
         yPosition -= 20;

         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("I am writing to refer my patient, "+refer.getPatientName()+", for further evaluation and treatment.");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("I have carefully reviewed the patient's medical history and current condition, ");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("and I believe that [he/she] would benefit from your expertise in ______________________.");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Patient's Condition/Diagnosis:");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("_________________________________________________________________");
         contentStream.endText();
         yPosition -= 20;
         
         contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Reason for Referral:");
         contentStream.endText();
         yPosition -= 20;
         
         
         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("________________________________________________________________");
         contentStream.endText();
         yPosition -= 20;
         yPosition -= 20;
         yPosition -= 20;
         
         
         contentStream.setFont(PDType1Font.HELVETICA, 12);
         contentStream.beginText();
         contentStream.newLineAtOffset(margin, yPosition);
         contentStream.showText("Thank you for your attention to this matter. I greatly appreciate your expertise and");
         contentStream.newLineAtOffset(0, -20);
         contentStream.showText("assistance in providing the best possible care for my patient. I will eagerly await your ");
         contentStream.newLineAtOffset(0, -20);
         contentStream.showText("evaluation and recommendations.");
         contentStream.newLineAtOffset(0, -20);
         contentStream.newLineAtOffset(0, -20);
         contentStream.showText("Sincerely,");
         contentStream.newLineAtOffset(0, -20);
         contentStream.newLineAtOffset(0, -20);
         contentStream.showText(refer.getDoctor());
         contentStream.newLineAtOffset(0, -20);
         contentStream.showText(refer.getHospital());
         contentStream.endText();
         
         
         // End the content stream
         contentStream.close();
         
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 	    document.save(byteArrayOutputStream);
 	    document.close();

 	    response.setContentType("application/pdf");
 	    response.setHeader("Content-Disposition", "attachment; filename=ReferNote.pdf");

 	    response.getOutputStream().write(byteArrayOutputStream.toByteArray());
 	    response.getOutputStream().flush();

    	
    }

}
