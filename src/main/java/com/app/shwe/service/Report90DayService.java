package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.model.Report90Day;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.Report90DayRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

@Service
public class Report90DayService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VisaServicesRepository visaRepo;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private Report90DayRepository reportRepo;
	
	public ResponseEntity<String> saveReport90Day(MultipartFile tm6Photo, MultipartFile expireDatePhoto,
			MultipartFile passportBio, MultipartFile visaPage,Report90DayRequestDTO request){
		if (tm6Photo == null && expireDatePhoto == null && passportBio == null &&  visaPage == null &&  request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			
			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));
			String tm6_photo = fileUploadService.uploadFile(tm6Photo);
			String expire_photo = fileUploadService.uploadFile(expireDatePhoto);
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			Report90Day report = new Report90Day();
			report.setTm6Photo(tm6_photo);
			report.setExpireDatePhoto(expire_photo);
			report.setPassportBio(passport_bio);
			report.setVisaPage(visa_page);
			report.setVisaType(request.getVisaType());
			report.setUser(user);
			report.setVisa(visaServices);
			reportRepo.save(report);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}

}
