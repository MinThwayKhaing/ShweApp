package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.shwe.dto.CarOrderRequestDTO;
import com.app.shwe.dto.VisaServiceRequestDTO;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.SubVisaTypeRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.repository.VsiaTypeRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class VisaTypeService {

	@Autowired
	private VsiaTypeRepository vsiaTypeRepository;

	@Autowired
	private SubVisaTypeRepository subVisaTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaService;

	@Transactional
	public ResponseEntity<String> saveVisaType(VisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {
			VisaServices visaServices = visaService.findById(request.getVisa_service_id())
					.orElseThrow(() -> new RuntimeException("CarRent not found for ID: " + request.getVisa_service_id()));
			VisaType visaType = new VisaType();
			visaType.setDescription(request.getDescription());
			visaType.setVisa(visaServices);
			visaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setCreatedDate(new Date());
			vsiaTypeRepository.save(visaType);
			SubVisaType subVisaType = new SubVisaType();
			subVisaType.setPrice(request.getPrice());
			subVisaType.setDuration(request.getDuration());
			subVisaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			subVisaType.setCreatedDate(new Date());
			subVisaType.setVisa(visaType);
			subVisaTypeRepository.save(subVisaType);

			return ResponseEntity.status(HttpStatus.OK).body("Visa saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving news: " + e.getMessage());
		}

	}

	@Transactional
	public List<VisaTypeResponseDTO> getVisaByType(VisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		List<VisaTypeResponseDTO> visaList = vsiaTypeRepository.findVisaByType(request.getDescription());
		List<VisaTypeResponseDTO> response = new ArrayList<>();
		for (VisaTypeResponseDTO visaTypeResponseDTO : visaList) {
			VisaTypeResponseDTO dto = new VisaTypeResponseDTO();
			VisaType type = new VisaType();
			type.setId(dto.getId());
			type.setDescription(dto.getDescription());
			dto.setId(visaTypeResponseDTO.getId());
			dto.setDescription(visaTypeResponseDTO.getDescription());
			SubVisaType sub = new SubVisaType();
			sub.setDuration(dto.getDuration());
			sub.setPrice(dto.getPrice());
			dto.setDuration(visaTypeResponseDTO.getDuration());
			dto.setPrice(visaTypeResponseDTO.getPrice());
			response.add(visaTypeResponseDTO);

		}

		return response;
	}

	@Transactional
	public List<VisaTypeResponseDTO> getAllVisaType() {
		List<VisaTypeResponseDTO> visaList = vsiaTypeRepository.findAllVisaType();
		List<VisaTypeResponseDTO> response = new ArrayList<>();
		for (VisaTypeResponseDTO visaTypeResponseDTO : visaList) {
			VisaTypeResponseDTO dto = new VisaTypeResponseDTO();
			VisaType type = new VisaType();
			type.setId(dto.getId());
			type.setDescription(dto.getDescription());
			dto.setId(visaTypeResponseDTO.getId());
			dto.setDescription(visaTypeResponseDTO.getDescription());
			SubVisaType sub = new SubVisaType();
			sub.setDuration(dto.getDuration());
			sub.setPrice(dto.getPrice());
			dto.setDuration(visaTypeResponseDTO.getDuration());
			dto.setPrice(visaTypeResponseDTO.getPrice());
			response.add(visaTypeResponseDTO);

		}

		return response;
	}

	@Transactional
	public ResponseEntity<String> updateVisaType(int id, VisaTypeRequestDTO request) {
		Optional<VisaType> getVisa = vsiaTypeRepository.findById(id);
		if (!getVisa.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		SubVisaType subVisa = subVisaTypeRepository.findById(request.getSubVisaId())
				.orElseThrow(() -> new RuntimeException("SubVisaType not found for ID: " + request.getSubVisaId()));

		try {
			VisaType visaType = getVisa.get();
			visaType.setDescription(request.getDescription());
			visaType.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setUpdatedDate(new Date());
			vsiaTypeRepository.save(visaType);
			subVisa.setDuration(request.getDuration());
			subVisa.setPrice(request.getPrice());
			subVisa.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			subVisa.setUpdatedDate(new Date());
			subVisa.setVisa(visaType);
			subVisaTypeRepository.save(subVisa);

			return new ResponseEntity<>("Visa updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public ResponseEntity<String> deleteVisaType(int id) {
		// Check if VisaType exists
		if (vsiaTypeRepository.checkVisaTypeById(id) == 0) {
			return new ResponseEntity<>("VisaType with ID " + id + " not found.", HttpStatus.NOT_FOUND);
		}

		try {
			// Delete associated SubVisaType records
			subVisaTypeRepository.deleteSubVisaTypesByVisaId(id);

			// Delete VisaType
			vsiaTypeRepository.deleteVisaTypeById(id);

			return new ResponseEntity<>("VisaType deleted successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while deleting VisaType: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
