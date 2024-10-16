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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.NewsRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TM30PeriodDTO;
import com.app.shwe.dto.Tm30DTO;
import com.app.shwe.dto.Tm30DTOResponseDTO;
import com.app.shwe.dto.Tm30DetailsDTO;
import com.app.shwe.dto.Tm30ProjectionDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.dto.Tm30ResponseDTO;
import com.app.shwe.dto.VisaResponseDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.EmbassyLetter;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.News;
import com.app.shwe.model.TM30Period;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaOrder;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.OrderKeyGeneratorService;
import com.app.shwe.repository.TM30PeriodRepository;
import com.app.shwe.repository.Tm30Repository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaOrderRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.OrderStatus;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;

@Service
public class Tm30Service {

	@Autowired
	private Tm30Repository tm30Repo;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaRepo;

	@Autowired
	private OrderKeyGeneratorService keyGenerator;

	@Autowired
	private VisaOrderRepository visaOrderRepository;

	@Autowired
	private MainOrderRepository mainOrderRepository;

	@Autowired
	private TM30PeriodRepository tm30PeriodRepo;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Transactional
	public ResponseEntity<String> saveTm30(MultipartFile passport, MultipartFile visa, Tm30RequestDTO request) {
		if (passport == null && visa == null && request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

			String imageUrl1 = fileUploadService.uploadFile(passport);
			String imageUrl2 = fileUploadService.uploadFile(visa);
			Tm30 tm30 = new Tm30();
			MainOrder mainOrder = new MainOrder();
			Optional<TM30Period> periodId = tm30PeriodRepo.findById(request.getPeriod());
			tm30.setSyskey(keyGenerator.generateVisaOrderId());
			tm30.setPeriod(periodId.get());
			tm30.setPassportBio(imageUrl1);
			tm30.setVisaPage(imageUrl2);
			tm30.setStatus("Pending");
			tm30.setContactNumber(request.getContactNumber());
			tm30.setCreatedBy(userId);
			tm30.setCreatedDate(new Date());
			tm30.setUser(user);
			tm30Repo.save(tm30);
			mainOrder.setPeriod(periodId.get().getDescription() + periodId.get().getPrice());
			mainOrder.setCreatedBy(userId);
			mainOrder.setCreatedDate(tm30.getCreatedDate());
			mainOrder.setStatus(tm30.getStatus());
			mainOrder.setSys_key(tm30.getSyskey());
			mainOrder.setOrder_id(tm30.getId());
			mainOrder.setUser(user);
			mainOrderRepository.save(mainOrder);
			return ResponseEntity.status(HttpStatus.OK).body("Tm-30 saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving TM-30: " + e.getMessage());
		}
	}

	public ResponseEntity<?> getTm30DetailsById(int id) {
		try {
			Tm30DetailsDTO tm30Details = tm30Repo.findTm30DetailsById(id);
			if (tm30Details != null) {
				return ResponseEntity.ok(tm30Details);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TM30 record not found with ID: " + id);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while fetching the TM30 details: " + e.getMessage());
		}
	}

	@Transactional
	public Optional<Tm30> getTm30ById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<Tm30> tm30 = tm30Repo.findById(id);
		return tm30;
	}
	
	@Transactional
    public Tm30ResponseDTO getOrderBySysKey(String sysKey) {
        return tm30Repo.findTm30ResponseBySyskey(sysKey);
    }

	@Transactional
	public Page<Tm30DTOResponseDTO> getTm30(String searchString, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return tm30Repo.getAllTm30(status, searchString, pageable);
	}

	// @Transactional
	// public Page<VisaResponseDTO> getAllTm30(SearchDTO search) {
	// int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
	// int size = search.getSize();
	// Tm30ResponseDTO dto = new Tm30ResponseDTO();
	// Pageable pageable = PageRequest.of(page, size);
	// Page<Tm30> tm30 = tm30Repo.getAllTm30(pageable);
	// List<VisaResponseDTO> response = new ArrayList<VisaResponseDTO>();
	// for (Tm30 tm : tm30) {
	// VisaResponseDTO visa = new VisaResponseDTO();
	// visa = tm30Repo.getTM30(tm.getId());
	// response.add(visa);
	// }
	// dto.setVisa(response);
	// return dto;
	// }
	//

	// @Transactional
	// public Page<VisaResponseDTO> getAllTm30(SearchDTO search) {
	// int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
	// int size = search.getSize();
	// Tm30ResponseDTO dto = new Tm30ResponseDTO();
	// Pageable pageable = PageRequest.of(page, size);
	// Page<Tm30> tm30 = tm30Repo.getAllTm30(pageable);
	// List<VisaResponseDTO> response = new ArrayList<VisaResponseDTO>();
	// for (Tm30 tm : tm30) {
	// VisaResponseDTO visa = new VisaResponseDTO();
	// visa = tm30Repo.getTM30(tm.getId());
	// response.add(visa);
	// }
	// dto.setVisa(response);
	// return dto;
	// }

	// @Transactional
	// public ResponseEntity<String> updateTm30(int id, MultipartFile passportPage,
	// MultipartFile visaPage,
	// Tm30RequestDTO request) {
	// Optional<Tm30> getTm30 = tm30Repo.findById(id);
	// if (!getTm30.isPresent()) {
	// throw new IllegalArgumentException("ID not found");
	// }

	// try {
	// Tm30 tm30 = getTm30.get();
	// String passportImage = fileUploadService.uploadFile(passportPage);
	// String visaImage = fileUploadService.uploadFile(visaPage);
	// tm30.setPeriodId(request.getPeriod());
	// tm30.setContactNumber(request.getContactNumber());
	// tm30.setDuration(request.getDuration());
	// tm30.setPassportBio(passportImage);
	// tm30.setVisaPage(visaImage);
	// tm30.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
	// tm30.setUpdatedDate(new Date());
	// tm30Repo.save(tm30);
	// return new ResponseEntity<>("Tm-30 updated successfully", HttpStatus.OK);
	// } catch (Exception e) {
	// return new ResponseEntity<>("Error occurred: " + e.getMessage(),
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }

	// }

	@Transactional
	public ResponseEntity<?> deleteTm30(int id) {
		int count = tm30Repo.checkTm30ById(id);

		if (count == 0) {
			return new ResponseEntity<>("Tm-30 with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			tm30Repo.deleteById(id);
			return new ResponseEntity<>("Tm-30 deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Transactional
	// public List<Tm30ResponseDTO> getTm30OrderByUserId() {
	// int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
	// List<Tm30ProjectionDTO> tm30List = tm30Repo.getTm30OrderByUserId(userId);

	// List<Tm30ResponseDTO> responseList = new ArrayList<>();
	// for (Tm30ProjectionDTO tm30 : tm30List) {
	// List<VisaResponseDTO> visaOrders =
	// tm30Repo.getVisaOrderByOrderId(tm30.getId());
	// Tm30ResponseDTO response = new Tm30ResponseDTO();
	// response.setTm30Order(tm30);
	// response.setVisaOrder(visaOrders);
	// responseList.add(response);
	// }
	// return responseList;
	// }

	// @Transactional
	// public List<Tm30ResponseDTO> getAllTm30Order() {
	// List<Tm30ProjectionDTO> tm30List = tm30Repo.getAllTm30Order();

	// List<Tm30ResponseDTO> responseList = new ArrayList<>();
	// for (Tm30ProjectionDTO tm30 : tm30List) {
	// List<VisaResponseDTO> visaOrders =
	// tm30Repo.getVisaOrderByOrderId(tm30.getId());
	// Tm30ResponseDTO response = new Tm30ResponseDTO();
	// response.setTm30Order(tm30);
	// response.setVisaOrder(visaOrders);
	// responseList.add(response);
	// }
	// return responseList;
	// }

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		String status = OrderStatus.Cancel_Order.name();
		Optional<Tm30> getTranslatorOrder = tm30Repo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			Tm30 model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			tm30Repo.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel Tm-30 order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Tm-30 order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> onProgress(int orderId) {
		String status = OrderStatus.ON_PROGRESS.name();
		Optional<Tm30> getTranslatorOrder = tm30Repo.findById(orderId);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			Tm30 model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			tm30Repo.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel Tm-30 order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Tm-30 order: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> completed(int id) {
		String status = OrderStatus.COMPLETED.name();
		Optional<Tm30> getTranslatorOrder = tm30Repo.findById(id);
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			Tm30 model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			tm30Repo.changeOrderStatus(id, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel Tm-30 order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Tm-30 order: " + e.getMessage());
		}
	}

	// public List<Tm30DTO> getTm30OrderByUserId(){
	// int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
	// return tm30Repo.getTm30OrderByUserId(userId);
	// }

}
