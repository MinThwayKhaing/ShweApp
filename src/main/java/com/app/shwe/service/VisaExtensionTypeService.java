package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.VisaExtensionTypeRequestDTO;
import com.app.shwe.dto.VisaExtensionTypeResponseDTO;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.VisaExtensionSubType;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaExtensionSubTypeRepository;
import com.app.shwe.repository.VisaExtensionTypeRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class VisaExtensionTypeService {
	@Autowired
	private VisaExtensionTypeRepository vsiaTypeRepository;

	@Autowired
	private VisaExtensionSubTypeRepository subVisaTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaService;

	public ResponseEntity<String> saveVisaType(VisaExtensionTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {
			VisaServices visaServices = visaService.findById(request.getVisa_service_id())
					.orElseThrow(() -> new RuntimeException(
							"Visa service not found for ID: " + request.getVisa_service_id()));

			VisaExtensionType visaType = new VisaExtensionType();
			visaType.setDescription(request.getDescription());
			visaType.setVisa(visaServices);
			visaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setCreatedDate(new Date());

			// Save the visaType first
			vsiaTypeRepository.save(visaType);

			// Now create and save the subVisaType
			VisaExtensionSubType subVisaType = new VisaExtensionSubType();
			subVisaType.setPrice(request.getPrice());
			subVisaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			subVisaType.setCreatedDate(new Date());
			subVisaType.setVisa(visaType); // Ensure visaType is saved and referenced

			subVisaTypeRepository.save(subVisaType);

			return ResponseEntity.status(HttpStatus.OK).body("Visa type saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving visa type: " + e.getMessage());
		}
	}

	// @Transactional
	// public List<VisaExtensionTypeResponseDTO>
	// getVisaByType(VisaExtensionTypeRequestDTO request) {
	// if (request == null) {
	// throw new IllegalArgumentException("Request and required fields must not be
	// null");
	// }
	// List<VisaExtensionTypeResponseDTO> visaList =
	// vsiaTypeRepository.findVisaByType(request.getDescription());
	// List<VisaExtensionTypeResponseDTO> response = new ArrayList<>();
	// for (VisaExtensionTypeResponseDTO visaTypeResponseDTO : visaList) {
	// VisaExtensionTypeResponseDTO dto = new VisaExtensionTypeResponseDTO();
	// VisaType type = new VisaType();
	// type.setId(dto.getId());
	// type.setDescription(dto.getDescription());
	// dto.setId(visaTypeResponseDTO.getId());
	// dto.setDescription(visaTypeResponseDTO.getDescription());
	// SubVisaType sub = new SubVisaType();
	// sub.setPrice(dto.getPrice());
	// dto.setPrice(visaTypeResponseDTO.getPrice());
	// response.add(visaTypeResponseDTO);

	// }

	// return response;
	// }

	// @Transactional
	// public List<VisaExtensionTypeResponseDTO> getAllVisaType() {
	// List<VisaExtensionTypeResponseDTO> visaList =
	// vsiaTypeRepository.findAllVisaType();
	// List<VisaExtensionTypeResponseDTO> response = new ArrayList<>();
	// for (VisaExtensionTypeResponseDTO visaTypeResponseDTO : visaList) {
	// VisaExtensionTypeResponseDTO dto = new VisaExtensionTypeResponseDTO();
	// VisaType type = new VisaType();
	// type.setId(dto.getId());
	// type.setDescription(dto.getDescription());
	// dto.setId(visaTypeResponseDTO.getId());
	// dto.setDescription(visaTypeResponseDTO.getDescription());
	// VisaExtensionSubType sub = new VisaExtensionSubType();
	// sub.setPrice(dto.getPrice());
	// dto.setPrice(visaTypeResponseDTO.getPrice());
	// response.add(visaTypeResponseDTO);

	// }

	// return response;
	// }

	@Transactional
	public ResponseEntity<String> updateVisaType(int id, VisaExtensionTypeRequestDTO request) {
		Optional<VisaExtensionType> getVisa = vsiaTypeRepository.findById(id);
		if (!getVisa.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		VisaExtensionSubType subVisa = subVisaTypeRepository.findById(request.getSubVisaId())
				.orElseThrow(() -> new RuntimeException("SubVisaType not found for ID: " + request.getSubVisaId()));

		try {
			VisaExtensionType visaType = getVisa.get();
			visaType.setDescription(request.getDescription());
			visaType.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setUpdatedDate(new Date());
			vsiaTypeRepository.save(visaType);
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
