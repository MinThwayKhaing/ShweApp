package com.app.shwe.service;

import java.util.ArrayList;
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

import com.app.shwe.dto.Report90DayDTO;
import com.app.shwe.dto.Report90DayProjectionDTO;
import com.app.shwe.dto.Report90DayRequestDTO;
import com.app.shwe.dto.Report90DayTypeResponseDTO;
import com.app.shwe.dto.Report90ResponseDTO;
import com.app.shwe.dto.Tm30ProjectionDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.dto.VisaExtensionDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.model.EmbassyLetter;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.Report90Day;
import com.app.shwe.model.Report90DayOrder;
import com.app.shwe.model.Report90DayVisaType;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaExtension;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.Report90DayOrderRepository;
import com.app.shwe.repository.Report90DayRepository;
import com.app.shwe.repository.Report90DayVisaTypeRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.OrderStatus;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;

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

	@Autowired
	private OrderGeneratorService orderGeneratorService;

	@Autowired
	private Report90DayOrderRepository orderRepository;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Autowired
	private Report90DayVisaTypeRepository report90_dayRepo;
	@Autowired
	MainOrderRepository mainOrderRepository;

	public ResponseEntity<String> saveReport90Day(MultipartFile tm6Photo, MultipartFile expireDatePhoto,
			MultipartFile passportBio, MultipartFile visaPage, Report90DayRequestDTO request) {
		if (tm6Photo == null && expireDatePhoto == null && passportBio == null && visaPage == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepo.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
			Optional<Report90DayVisaType> visaType = report90_dayRepo.findById(request.getVisaType());
			Report90DayOrder order = new Report90DayOrder();
			String tm6_photo = fileUploadService.uploadFile(tm6Photo);
			String expire_photo = fileUploadService.uploadFile(expireDatePhoto);
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			Report90Day report = new Report90Day();

			report.setSyskey(orderGeneratorService.generateReport90DayOrderId());
			report.setTm6Photo(tm6_photo);
			report.setExpireDatePhoto(expire_photo);
			report.setVisaType(visaType.get());
			report.setVisaTypeDescription(visaType.get().getId());
			report.setPassportBio(passport_bio);
			report.setVisaPage(visa_page);
			report.setStatus("Pending");
			report.setCreatedBy(userId);
			report.setUser(user);

			reportRepo.save(report);
			MainOrder mainOrder = new MainOrder();
			mainOrder.setVisaType(visaType.get().getDescription() + visaType.get().getPrice());
			mainOrder.setCreatedBy(userId);
			mainOrder.setCreatedDate(report.getCreatedDate());
			mainOrder.setStatus(report.getStatus());
			mainOrder.setSys_key(report.getSyskey());
			mainOrder.setOrder_id(report.getId());
			mainOrder.setUser(user);
			mainOrderRepository.save(mainOrder);
			orderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("90 Days Report saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving TM-30: " + e.getMessage());
		}

	}
	// For Repair
	// @Transactional
	// public List<Report90ResponseDTO> getReport90DayOrder() {
	// int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
	// List<Report90DayProjectionDTO> reportList =
	// reportRepo.getReport90DayOrderByUserId(userId);

	// List<Report90ResponseDTO> responseList = new ArrayList<>();
	// for (Report90DayProjectionDTO report : reportList) {
	// List<Report90DayTypeResponseDTO> visaOrders =
	// reportRepo.getVisaOrderByOrderId(report.getId());
	// Report90ResponseDTO response = new Report90ResponseDTO();
	// response.setReportOrder(report);
	// response.setVisaOrder(visaOrders);
	// responseList.add(response);
	// }
	// return responseList;
	// }

	@Transactional
	public ResponseEntity<?> deleteReport90DayById(int id) {
		int count = reportRepo.checkReport90DayById(id);

		if (count == 0) {
			return new ResponseEntity<>("Report 90 Days with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			reportRepo.deleteById(id);
			orderRepository.deleteOrderById(id);
			return new ResponseEntity<>("Report 90 Days deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public Page<Report90DayDTO> getAllReport90DayVisa(String searchString, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return reportRepo.getAllVisa(status, searchString, pageable);
	}

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		String status = OrderStatus.Cancel_Order.name();
		Optional<Report90Day> getTranslatorOrder = reportRepo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		try {

			Report90Day model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			reportRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is canceled.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> completed(int orderId) {
		String status = OrderStatus.COMPLETED.name();
		Optional<Report90Day> getTranslatorOrder = reportRepo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		try {

			Report90Day model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			reportRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is completed");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> onProgress(int orderId) {
		String status = OrderStatus.ON_PROGRESS.name();
		Optional<Report90Day> getTranslatorOrder = reportRepo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		try {

			Report90Day model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			reportRepo.changeOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Order is On Progress");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel 90 Day Report order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<Report90DayDTO> getReport90DayOrderById(String sysKey) {
		Optional<Report90DayDTO> visaTypeOpt = reportRepo.getVisaOrderById(sysKey);
		if (!visaTypeOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
		}

		return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

}
