package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.EmbassyLetterDTO;
import com.app.shwe.dto.EmbassyLetterProjectionDTO;
import com.app.shwe.dto.EmbassyLetterRequestDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.EmbassyResponseDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.model.EmbassyLetter;
import com.app.shwe.model.EmbassyLetterOrder;
import com.app.shwe.model.EmbassyVisaType;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.Report90Day;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaExtension;
import com.app.shwe.model.VisaExtensionOrder;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.EmbassyLetterOrderRepository;
import com.app.shwe.repository.EmbassyLetterRepository;
import com.app.shwe.repository.EmbassyVisaTypeRepository;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaExtensionOrderRepository;
import com.app.shwe.repository.VisaExtensionRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.OrderStatus;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;

@Service
public class EmbassyLetterService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private VisaServicesRepository visaRepo;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private EmbassyLetterRepository embassyRepo;

	@Autowired
	private OrderGeneratorService orderGeneratorService;

	@Autowired
	private EmbassyLetterOrderRepository orderRepository;

	@Autowired
	private MainOrderRepository mainOrderRepository;
	
	@Autowired
	private EmbassyVisaTypeRepository vsiaTypeRepository;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Transactional
	public ResponseEntity<String> saveEmbassyLetter(MultipartFile passportBio, MultipartFile visaPage,
			EmbassyLetterRequestDTO request) {
		if (passportBio == null && visaPage == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			MainOrder mainOrder = new MainOrder();
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			
			Optional<EmbassyVisaType> visaType =vsiaTypeRepository.findById(request.getVisa_id());
			EmbassyLetterOrder order = new EmbassyLetterOrder();
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			EmbassyLetter embassy = new EmbassyLetter();
			embassy.setSyskey(orderGeneratorService.generateEmbassyOrderId());
			embassy.setPassportBio(passport_bio);
			embassy.setVisaPage(visa_page);
			embassy.setVisaType(visaType.get());
			embassy.setVisaTypeDescription(visaType.get().getDescription());
			embassy.setStatus("Pending");
			embassy.setAddress(request.getAddress());
			embassy.setContactNumber(request.getContactNumber());
			embassy.setUser(user);
			embassy.setCreatedBy(userId);
			embassy.setCreatedDate(new Date());
			embassyRepo.save(embassy);
			mainOrder.setCreatedBy(userId);
			mainOrder.setRecommendationLetterType(embassy.getVisaTypeDescription());
			mainOrder.setCreatedDate(embassy.getCreatedDate());
			mainOrder.setStatus(embassy.getStatus());
			mainOrder.setSys_key(embassy.getSyskey());
			mainOrder.setOrder_id(embassy.getId());
			mainOrder.setUser(user);
			mainOrderRepository.save(mainOrder);
			orderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Embassy Recommendation Letter saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving Embassy Recommendation Letter: " + e.getMessage());
		}

	}
	
//	@Transactional
//    public List<EmbassyLetterDTO> getEmbassyLetterOrderByUserId(){
//    	int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
//    	return embassyRepo.getEmbassyLetterByUserId(userId);
//    }

//	@Transactional
//	public List<EmbassyLetterResponseDTO> getEmbassyLetterOrderByUserId() {
//		int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
//		List<EmbassyLetterProjectionDTO> visaList = embassyRepo.getEmbassyLetterByUserId(userId);
//
//		List<EmbassyLetterResponseDTO> responseList = new ArrayList<>();
//		for (EmbassyLetterProjectionDTO dto : visaList) {
//			List<EmbassyResponseDTO> visaOrders = embassyRepo.getVisaOrderByOrderId(dto.getId());
//			EmbassyLetterResponseDTO response = new EmbassyLetterResponseDTO();
//			response.setEmbassyLetter(dto);
//			response.setEmbassyLetterOrder(visaOrders);
//			responseList.add(response);
//		}
//		return responseList;
//	}
	
	@Transactional
	public Page<EmbassyLetterDTO> getAllEmbassyVisaOrder(String searchString, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return embassyRepo.getAllVisa(status, searchString, pageable);
	}
	

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		String status = OrderStatus.Cancel_Order.name();
		Optional<EmbassyLetter> getEmbassyLetter = embassyRepo.findById(orderId);
		EmbassyLetter model = getEmbassyLetter.get();
		if (!getEmbassyLetter.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			embassyRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cancel Embassy Recommendation Letter order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Embassy Recommendation Letter order: " + e.getMessage());
		}
	}
	
	@Transactional
	public ResponseEntity<String> completed(int orderId) {
		String status = OrderStatus.COMPLETED.name();
		Optional<EmbassyLetter> getEmbassyLetter = embassyRepo.findById(orderId);
		if (!getEmbassyLetter.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		try {

			EmbassyLetter model = getEmbassyLetter.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			embassyRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}
	
	@Transactional
	public ResponseEntity<String> onProgress(int orderId) {
		String status = OrderStatus.ON_PROGRESS.name();
		Optional<EmbassyLetter> getEmbassyLetter = embassyRepo.findById(orderId);
		if (!getEmbassyLetter.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		try {

			EmbassyLetter model = getEmbassyLetter.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			embassyRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is On Progress");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<EmbassyLetterDTO> getEmbassyLetterOrderById(int id) {
	    Optional<EmbassyLetterDTO> visaTypeOpt = embassyRepo.getVisaOrder(id);
	    if (!visaTypeOpt.isPresent()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
	    }
	    
	    return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

}
