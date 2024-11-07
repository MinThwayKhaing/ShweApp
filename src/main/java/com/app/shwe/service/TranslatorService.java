package com.app.shwe.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.datamapping.TranslatorOrderMapping;
import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.ResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderDetailResponseDTO;
import com.app.shwe.dto.TranslatorOrderRequestDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.Translator;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.TranslatorOrderRepostitory;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.FilesSerializationUtil;
import com.app.shwe.utils.OrderStatus;
import com.app.shwe.utils.SecurityUtils;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Service
public class TranslatorService {

	@Autowired
	private TranslatorRepository translatorRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private TranslatorOrderMapping orderMapping;

	@Autowired
	private TranslatorOrderRepostitory transOrderRepository;

	@Autowired
	private UserRepository userRepo;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Autowired
	MainOrderRepository mainOrderRepository;

	@Transactional
	public ResponseEntity<String> saveTranslator(MultipartFile image, TranslatorRequestDTO request) {
		try {
			if (request == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request data is null.");
			}
			String imageUrl = fileUploadService.uploadFile(image);
			Translator translator = new Translator();
			translator.setImage(imageUrl);
			translator.setName(request.getName());
			translator.setLanguage(request.getLanguage());
			translator.setSpecialist(request.getSpecialist());
			translator.setCreatedDate(new Date());
			translator.setPhoneNumber(request.getPhoneNumber());
			translator.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			translatorRepo.save(translator);
			return ResponseEntity.status(HttpStatus.OK).body("Translator saved successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}

	}

	@Transactional
	public ResponseEntity<Translator> getTranslatorById(Integer id) {
		System.out.println(id);
		Optional<Translator> translator = translatorRepo.findById(id);
		if (!translator.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		System.out.println(translator.get());
		return new ResponseEntity<>(translator.get(), HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<String> updateTranslator(int id, MultipartFile image, TranslatorRequestDTO request) {
		Optional<Translator> getTranslator = translatorRepo.findById(id);

		if (!getTranslator.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			Translator translator = getTranslator.get();
			if (image != null && !image.isEmpty()) {
				String imageUrl = fileUploadService.uploadFile(image);
				translator.setImage(imageUrl);
			}
			translator.setPhoneNumber(request.getPhoneNumber());
			translator.setName(request.getName());
			translator.setLanguage(request.getLanguage());
			translator.setSpecialist(request.getSpecialist());
			translator.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			translator.setUpdatedDate(new Date());
			translatorRepo.save(translator); // Save the updated translator
			return new ResponseEntity<>("Translator updated successfully", HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	public ResponseEntity<?> deleteTranslator(int id) {
		int count = translatorRepo.checkTranslator(id);
		if (count == 0) {
			return new ResponseEntity<>("Translator with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}
		try {
			Optional<Translator> translatorOptional = translatorRepo.findById(id);
			if (translatorOptional.isPresent()) {
				Translator translator = translatorOptional.get();
				translator.setDeleteStatus(true); // Set the delete status to true
				translatorRepo.save(translator); // Save the updated translator
				return new ResponseEntity<>("Translator deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Translator with ID " + id + " not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public Page<Translator> searchTranslator(String searchString, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return translatorRepo.searchTranslator(searchString, pageable);
	}

	@Transactional
	public Page<Translator> searchTranslatorAdmin(String date, String searchString, int page, int size) {
		Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size);
		return translatorRepo.searchTranslator(searchString, pageable);
	}

	@Transactional
	public ResponseEntity<String> hireTranslator(TranslatorOrderRequestDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		dto.setStatus("Pending");
		try {
			TranslatorOrder order = orderMapping.mapToTranslatorOrder(dto);

			// MainOrder mainorder = new MainOrder();
			// mainorder.setOrder_id(order.getId());
			// mainorder.setSys_key(order.getSysKey());
			// mainorder.setUser(order.getUser());
			// mainorder.setDeleteStatus(false);
			// mainorder.setStatus(order.getStatus());
			// mainOrderRepository.save(mainorder);
			return ResponseEntity.status(HttpStatus.OK).body("Translator order saved successfully.");
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public Page<TranslatorOrderResponseDTO> searchHireTranslator(SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return transOrderRepository.searchHireTranslator(searchString, pageable);
	}

	@Transactional
	public Page<TranslatorOrderResponseDTO> findOrderByUserId(int id, SearchDTO dto) {
		String searchString = dto.getSearchString();
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return transOrderRepository.findOrderByUserId(id, searchString, pageable);
	}

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		String status = OrderStatus.Cancel_Order.name();
		Optional<TranslatorOrder> getTranslatorOrder = transOrderRepository.findById(orderId);

		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			TranslatorOrder model = getTranslatorOrder.get();
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSysKey());
			transOrderRepository.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body("Cancel translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> updateTranslatorOrder(int orderId, TranslatorOrderRequestDTO request) {
		Optional<TranslatorOrder> getTranslatorOrder = Optional.ofNullable(getOrderForUpdate(orderId));
		if (!getTranslatorOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Id not found ");
		}
		Translator translator = translatorRepo.findById(request.getTranslator_id())
				.orElseThrow(() -> new RuntimeException("Translator not found for ID: " + request.getTranslator_id()));
		try {
			TranslatorOrder order = getTranslatorOrder.get();
			order.setFromDate(request.getFromDate());
			order.setToDate(request.getToDate());
			order.setUsedFor(request.getUsedFor());
			order.setMeetingPoint(request.getMeetingPoint());
			order.setMeetingDate(request.getMeetingDate());
			order.setMeetingTime(request.getMeetingTime());
			order.setPhoneNumber(request.getPhoneNumber());
			order.setStatus(request.getStatus());
			order.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			order.setCreatedDate(new Date());
			order.setTranslator(translator);

			transOrderRepository.save(order);
			return ResponseEntity.status(HttpStatus.OK).body("Update translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<String> updateTranslatorOrderFromAdmin(int orderId, TranslatorOrderRequestDTO request) {
		String status = OrderStatus.ON_PROGRESS.name();
		Optional<TranslatorOrder> getTranslatorOrder = Optional.ofNullable(getOrderForUpdate(orderId));
		if (!getTranslatorOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Id not found ");
		}
		Translator translator = translatorRepo.findById(request.getTranslator_id())
				.orElseThrow(() -> new RuntimeException("Translator not found for ID: " + request.getTranslator_id()));
		try {
			TranslatorOrder order = getTranslatorOrder.get();
			order.setStatus(status);
			order.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			order.setTranslator(translator);

			transOrderRepository.save(order);

			Optional<MainOrder> mainorder = mainOrderRepository.findByOrderIdAndSysKey(orderId, request.getSysKey());

			if (mainorder.isPresent()) {
				MainOrder model = mainorder.get();
				model.setStatus(status);
				mainOrderRepository.save(model);
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Order Not Found: " + request.getSysKey());
			}

			return ResponseEntity.status(HttpStatus.OK).body("Update translator order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving translator: " + e.getMessage());
		}
	}

	@Transactional
	public Page<TranslatorOrderResponseDTO> getHireTranslatorById(SearchDTO dto) {
		String searchString = dto.getSearchString();
		int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
		int page = (dto.getPage() < 1) ? 0 : dto.getPage() - 1;
		int size = dto.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return transOrderRepository.findOrderByUserId(userId, searchString, pageable);
	}

	public ResponseEntity<?> findTranslatorOrderBySysKey(String sysKey) {
		try {
			// Fetch the result from the repository, which returns an Object[]
			Object[] result = transOrderRepository.findTranslatorOrderBySysKey(sysKey);

			// Check if the result is null or empty
			if (result == null || result.length == 0) {
				return new ResponseEntity<>("Order not found with sysKey: " + sysKey, HttpStatus.NOT_FOUND);
			}

			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public TranslatorOrder getOrderForUpdate(int id) {
		return transOrderRepository.findByIdForUpdate(id);
	}

}
