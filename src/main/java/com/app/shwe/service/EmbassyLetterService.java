package com.app.shwe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.EmbassyLetterProjectionDTO;
import com.app.shwe.dto.EmbassyLetterRequestDTO;
import com.app.shwe.dto.EmbassyLetterResponseDTO;
import com.app.shwe.dto.EmbassyResponseDTO;
import com.app.shwe.dto.VisaExtensionOrderResponseDTO;
import com.app.shwe.dto.VisaExtensionProjectionDTO;
import com.app.shwe.dto.VisaExtensionRequestDTO;
import com.app.shwe.dto.VisaExtensionResponseDTO;
import com.app.shwe.model.EmbassyLetter;
import com.app.shwe.model.EmbassyLetterOrder;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaExtension;
import com.app.shwe.model.VisaExtensionOrder;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.EmbassyLetterOrderRepository;
import com.app.shwe.repository.EmbassyLetterRepository;
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

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

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

			VisaServices visaServices = visaRepo.findById(request.getVisa_id())
					.orElseThrow(() -> new IllegalArgumentException("Visa not found with id: " + request.getVisa_id()));
			EmbassyLetterOrder order = new EmbassyLetterOrder();
			String passport_bio = fileUploadService.uploadFile(passportBio);
			String visa_page = fileUploadService.uploadFile(visaPage);
			EmbassyLetter embassy = new EmbassyLetter();
			embassy.setSyskey(orderGeneratorService.generateEmbassyOrderId());
			embassy.setPassportBio(passport_bio);
			embassy.setVisaPage(visa_page);
			embassy.setStatus("Pending");
			embassy.setUser(user);
			embassy.setVisa(visaServices);
			embassyRepo.save(embassy);
			if (request.getOption1() == 1) {
				order.setSub_visa_id(request.getOption1());
			} else if (request.getOption2() == 2) {
				order.setSub_visa_id(request.getOption2());
			} else if (request.getOption3() == 3) {
				order.setSub_visa_id(request.getOption3());
			} else if (request.getOption4() == 4) {
				order.setSub_visa_id(request.getOption4());
			} else if (request.getOption5() == 5) {
				order.setSub_visa_id(request.getOption5());
			} else if (request.getOption6() == 6) {
				order.setSub_visa_id(request.getOption6());
			} else if (request.getOption7() == 7) {
				order.setSub_visa_id(request.getOption7());
			} else if (request.getOption8() == 8) {
				order.setSub_visa_id(request.getOption8());
			} else if (request.getOption9() == 9) {
				order.setSub_visa_id(request.getOption9());
			} else if (request.getOption10() == 10) {
				order.setSub_visa_id(request.getOption10());
			} else if (request.getOption11() == 11) {
				order.setSub_visa_id(request.getOption11());
			}
			order.setMain_visa_id(request.getVisa_id());
			order.setOrder_id(embassy.getId());
			mainOrder.setPeriod(embassy.getPeriod());
			mainOrder.setCreatedBy(userId);
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

	@Transactional
	public List<EmbassyLetterResponseDTO> getEmbassyLetterOrderByUserId() {
		int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
		List<EmbassyLetterProjectionDTO> visaList = embassyRepo.getEmbassyLetterByUserId(userId);

		List<EmbassyLetterResponseDTO> responseList = new ArrayList<>();
		for (EmbassyLetterProjectionDTO dto : visaList) {
			List<EmbassyResponseDTO> visaOrders = embassyRepo.getVisaOrderByOrderId(dto.getId());
			EmbassyLetterResponseDTO response = new EmbassyLetterResponseDTO();
			response.setEmbassyLetter(dto);
			response.setEmbassyLetterOrder(visaOrders);
			responseList.add(response);
		}
		return responseList;
	}

	@Transactional
	public ResponseEntity<String> cancelOrder(int orderId) {
		String status = OrderStatus.Cancel_Order.name();
		Optional<EmbassyLetter> getTranslatorOrder = embassyRepo.findById(orderId);
		EmbassyLetter model = getTranslatorOrder.get();
		if (!getTranslatorOrder.isPresent()) {
			return new ResponseEntity<>("Error occurred: ", HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try {
			mainOrderRepository.updateOrderStatusToOnProgress(status, model.getSyskey());
			embassyRepo.cancelOrder(orderId, status);
			return ResponseEntity.status(HttpStatus.OK)
					.body("Cancel Embassy Recommendation Letter order successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while cancel Embassy Recommendation Letter order: " + e.getMessage());
		}
	}

}
