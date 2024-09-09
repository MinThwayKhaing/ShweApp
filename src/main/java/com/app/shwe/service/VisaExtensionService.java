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
import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.Report90ResponseDTO;
import com.app.shwe.dto.Tm30DTOResponseDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.Report90Day;
import com.app.shwe.model.Report90DayOrder;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaExtension;
import com.app.shwe.model.VisaExtensionOrder;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.Report90DayOrderRepository;
import com.app.shwe.repository.Report90DayRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaExtensionOrderRepository;
import com.app.shwe.repository.VisaExtensionRepository;
import com.app.shwe.repository.VisaExtensionTypeRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.OrderStatus;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Autowired
	private VisaExtensionTypeRepository vsiaTypeRepository;

	@Autowired
	private MainOrderRepository mainOrderRepository;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	public ResponseEntity<String> saveVisaExtension(MultipartFile passportBio, MultipartFile visaPage,
			VisaExtensionRequestDTO request) {
		if (passportBio == null && visaPage == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			MainOrder mainOrder = new MainOrder();
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));

			// VisaExtensionType visaType =
			// vsiaTypeRepository.findVisaExtensionTypeById(request.getVisa_id());
			Optional<VisaExtensionType> visaType = vsiaTypeRepository.findById(request.getVisa_id());
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			VisaExtension visaExtension = new VisaExtension();
			visaExtension.setSyskey(orderGeneratorService.generateVisaExtensionOrderId());
			visaExtension.setPassportBio(passport_bio);
			visaExtension.setVisaPage(visa_page);
			visaExtension.setVisaType(visaType.get());
			visaExtension.setVisaTypeDescription(visaType.get().getId());
			visaExtension.setStatus("Pending");
			visaExtension.setUser(user);
			visaExtension.setContactNumber(request.getContactNumber());
			visaExtension.setCreatedBy(userId);
			visaExtension.setCreatedDate(new Date());
			;

			visaExtensionRepo.save(visaExtension);
			mainOrder.setPeriod(visaExtension.getPeriod());
			mainOrder.setVisaType(visaType.get().getDescription() + visaType.get().getPrice());
			mainOrder.setCreatedBy(userId);
			mainOrder.setCreatedDate(visaExtension.getCreatedDate());
			mainOrder.setStatus(visaExtension.getStatus());
			mainOrder.setSys_key(visaExtension.getSyskey());
			mainOrder.setOrder_id(visaExtension.getId());
			mainOrder.setUser(user);
			mainOrderRepository.save(mainOrder);

			return ResponseEntity.status(HttpStatus.OK).body("Visa Extension saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving Visa Extension: " + e.getMessage());
		}

	}

	// List<VisaServices> visaList = visaRepo.getAllVisaService();
	// List<VisaServices> visa = new ArrayList<>();
	// for (VisaServices visaService : visaList) {
	// visa.add(visaService);
	// }
	// return visaList;

	// public List<VisaExtensionDTO> getVisaExtensionByOrder() {
	// int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
	// return visaExtensionRepo.getVisaExtensionOrder(userId);
	// }

	// @Transactional
	// public List<VisaExtensionResponseDTO> getVisaExtensionByOrder() {
	// int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
	// List<VisaExtensionProjectionDTO> visaList =
	// visaExtensionRepo.getVisaExtensionOrderByUserId(userId);
	//
	// List<VisaExtensionResponseDTO> responseList = new ArrayList<>();
	// for (VisaExtensionProjectionDTO dto : visaList) {
	// List<VisaExtensionOrderResponseDTO> visaOrders =
	// visaExtensionRepo.getVisaOrderByOrderId(dto.getId());
	// VisaExtensionResponseDTO response = new VisaExtensionResponseDTO();
	// response.setVisaExtension(dto);
	// response.setVisaOrder(visaOrders);
	// responseList.add(response);
	// }
	// return responseList;
	// }

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		Optional<VisaExtension> getTranslatorOrder = visaExtensionRepo.findById(orderId);
		String status = OrderStatus.Cancel_Order.name();
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			VisaExtension model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			visaExtensionRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is canceled");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}

	@Transactional
	public Page<VisaExtensionDTO> getAllVisaExtensionOrder(String searchString, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return visaExtensionRepo.getAllVisa(status, searchString, pageable);
	}

	@Transactional
	public ResponseEntity<String> onProgress(int orderId) {
		String status = OrderStatus.ON_PROGRESS.name();
		Optional<VisaExtension> getVisa = visaExtensionRepo.findById(orderId);
		if (!getVisa.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try {
			VisaExtension model = getVisa.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			visaExtensionRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is On Progress");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Tm-30 order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> completed(int orderId) {
		String status = OrderStatus.COMPLETED.name();
		Optional<VisaExtension> getVisa = visaExtensionRepo.findById(orderId);
		if (!getVisa.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			VisaExtension model = getVisa.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			visaExtensionRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Tm-30 order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<VisaExtensionDTO> getVisaExtensionOrderById(String sysKey) {
		Optional<VisaExtensionDTO> visaTypeOpt = visaExtensionRepo.getVisaOrderById(sysKey);
		if (!visaTypeOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
		}

		return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

}
