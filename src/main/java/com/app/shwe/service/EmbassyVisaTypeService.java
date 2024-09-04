package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.EmbassyVisaTypeRequestDTO;
import com.app.shwe.dto.EmbassyVisaTypeResponseDTO;
import com.app.shwe.model.EmbassySubVisaType;
import com.app.shwe.model.EmbassyVisaType;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.VisaExtensionSubType;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.EmbassySubVisaTypeRepository;
import com.app.shwe.repository.EmbassyVisaTypeRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class EmbassyVisaTypeService {

	@Autowired
	private EmbassyVisaTypeRepository vsiaTypeRepository;

	@Autowired
	private EmbassySubVisaTypeRepository subVisaTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaService;

	public ResponseEntity<String> saveVisaType(EmbassyVisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {
			// VisaServices visaServices =
			// visaService.findById(request.getVisa_service_id())
			// .orElseThrow(() -> new RuntimeException("Visa service not found for ID: " +
			// request.getVisa_service_id()));
			//
			EmbassyVisaType visaType = new EmbassyVisaType();
			visaType.setDescription(request.getDescription());
			visaType.setPrice(request.getPrice());
			// visaType.setDelete_status(0);
			visaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setCreatedDate(new Date());

			// Save the visaType first
			vsiaTypeRepository.save(visaType);

			// Save the visaType first
			vsiaTypeRepository.save(visaType);

			return ResponseEntity.status(HttpStatus.OK).body("Embassy Recommendation Letter saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving Embassy Recommendation Letter: " + e.getMessage());
		}
	}

	@Transactional
	public List<EmbassyVisaTypeResponseDTO> getVisaByType(EmbassyVisaTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		List<EmbassyVisaTypeResponseDTO> visaList = vsiaTypeRepository.findVisaByType(request.getDescription());
		List<EmbassyVisaTypeResponseDTO> response = new ArrayList<>();
		for (EmbassyVisaTypeResponseDTO visaTypeResponseDTO : visaList) {
			EmbassyVisaTypeResponseDTO dto = new EmbassyVisaTypeResponseDTO();
			VisaType type = new VisaType();
			type.setId(dto.getId());
			type.setDescription(dto.getDescription());
			dto.setId(visaTypeResponseDTO.getId());
			dto.setDescription(visaTypeResponseDTO.getDescription());
			SubVisaType sub = new SubVisaType();
			sub.setPrice(dto.getPrice());
			dto.setPrice(visaTypeResponseDTO.getPrice());
			response.add(visaTypeResponseDTO);

		}

		return response;
	}

	@Transactional
	public ResponseEntity<String> updateVisaType(int id, EmbassyVisaTypeRequestDTO request) {
		Optional<EmbassyVisaType> getVisa = vsiaTypeRepository.findById(id);
		if (!getVisa.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}
		try {
			EmbassyVisaType visaType = getVisa.get();
			visaType.setDescription(request.getDescription());
			visaType.setPrice(request.getPrice());
			visaType.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setUpdatedDate(new Date());
			vsiaTypeRepository.save(visaType);

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

	@Transactional
	public ResponseEntity<EmbassyVisaType> getEmbassyLetteById(int id) {
		Optional<EmbassyVisaType> visaTypeOpt = vsiaTypeRepository.findById(id);
		if (!visaTypeOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
		}

		return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

	@Transactional
	public List<EmbassyVisaType> getAllVisaType() {
		return vsiaTypeRepository.getAllVisa();
	}

}
