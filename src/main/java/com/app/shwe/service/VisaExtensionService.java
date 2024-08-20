package com.app.shwe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.Report90ResponseDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.Report90Day;
import com.app.shwe.model.Report90DayOrder;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaExtension;
import com.app.shwe.model.VisaExtensionOrder;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.Report90DayOrderRepository;
import com.app.shwe.repository.Report90DayRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaExtensionOrderRepository;
import com.app.shwe.repository.VisaExtensionRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class VisaExtensionService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VisaServicesRepository visaRepo;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private VisaExtensionRepository visaExtensionRepo;
	
	@Autowired
	private OrderGeneratorService orderGeneratorService;
	
	@Autowired
	private VisaExtensionOrderRepository orderRepository;
	
	public ResponseEntity<String> saveVisaExtension(MultipartFile passportBio, MultipartFile visaPage,VisaExtensionRequestDTO request){
		if (passportBio == null &&  visaPage == null &&  request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			
			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));
			VisaExtensionOrder order = new VisaExtensionOrder();
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			VisaExtension visaExtension = new VisaExtension();
			visaExtension.setSyskey(orderGeneratorService.generateVisaExtensionOrderId());
			visaExtension.setPassportBio(passport_bio);
			visaExtension.setVisaPage(visa_page);
			visaExtension.setStatus("Pending");
			visaExtension.setUser(user);
			visaExtension.setVisa(visaServices);
			visaExtensionRepo.save(visaExtension);
			if(request.getOption1() == 1) {
			    order.setSub_visa_id(request.getOption1());
			} else if(request.getOption2() == 2) {
			    order.setSub_visa_id(request.getOption2());
			} else if(request.getOption3() == 3) {
			    order.setSub_visa_id(request.getOption3());
			}else if(request.getOption4() == 4) {
			    order.setSub_visa_id(request.getOption4());
			}else if(request.getOption5() == 5) {
			    order.setSub_visa_id(request.getOption5());
			}else if(request.getOption6() == 6) {
			    order.setSub_visa_id(request.getOption6());
			}else if(request.getOption7() == 7) {
			    order.setSub_visa_id(request.getOption7());
			}
			order.setMain_visa_id(request.getVisa_id());
			order.setOrder_id(visaExtension.getId());
			orderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Visa Extension saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving Visa Extension: " + e.getMessage());
		}
		
	}
	
	
	@Transactional
	public List<VisaExtensionResponseDTO> getVisaExtensionByOrder() {
	    int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
	    List<VisaExtensionProjectionDTO> visaList = visaExtensionRepo.getVisaExtensionOrderByUserId(userId);
	    
	    List<VisaExtensionResponseDTO> responseList = new ArrayList<>();
	    for (VisaExtensionProjectionDTO dto : visaList) {
	        List<VisaExtensionOrderResponseDTO> visaOrders = visaExtensionRepo.getVisaOrderByOrderId(dto.getId());
	        VisaExtensionResponseDTO response = new VisaExtensionResponseDTO();
	        response.setVisaExtension(dto);
	        response.setVisaOrder(visaOrders);
	        responseList.add(response);
	    }
	    return responseList;
	}
	
	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		Optional<VisaExtension> getTranslatorOrder = visaExtensionRepo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			String status = "Cancel Order";
			visaExtensionRepo.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel Visa Extension order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}
	
	

}
