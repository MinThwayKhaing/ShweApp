package com.app.shwe.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.Report90DayTypeRequestDTO;
import com.app.shwe.dto.Report90DayTypeResponseType;
import com.app.shwe.dto.VisaTypeRequestDTO;
import com.app.shwe.dto.VisaTypeResponseDTO;
import com.app.shwe.model.EmbassyVisaType;
import com.app.shwe.model.Report90DaySubVisaType;
import com.app.shwe.model.Report90DayVisaType;
import com.app.shwe.model.SubVisaType;
import com.app.shwe.model.VisaExtensionType;
import com.app.shwe.model.VisaServices;
import com.app.shwe.model.VisaType;
import com.app.shwe.repository.Report90DaySubVisaRepository;
import com.app.shwe.repository.Report90DayTypeRepository;
import com.app.shwe.repository.Report90DayVisaTypeRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.repository.VisaServicesRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class Report90DayVisaTypeService {

	@Autowired
	private Report90DayTypeRepository vsiaTypeRepository;

	@Autowired
	private Report90DaySubVisaRepository subVisaTypeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VisaServicesRepository visaService;

	@Autowired
	private Report90DayVisaTypeRepository vsTypeRepo;

	public ResponseEntity<String> saveVisaType(Report90DayTypeRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}

		try {

			Report90DayVisaType visaType = new Report90DayVisaType();
			visaType.setDescription(request.getDescription());
			visaType.setPrice(request.getPrice());
			visaType.setDelete_status(0);
			visaType.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setCreatedDate(new Date());

			// Save the visaType first
			vsiaTypeRepository.save(visaType);

			return ResponseEntity.status(HttpStatus.OK).body("Visa type saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving visa type: " + e.getMessage());
		}
	}

	// @Transactional
	// public List<Report90DayTypeResponseType>
	// getVisaByType(Report90DayTypeRequestDTO request) {
	// if (request == null) {
	// throw new IllegalArgumentException("Request and required fields must not be
	// null");
	// }
	// List<Report90DayTypeResponseType> visaList =
	// vsiaTypeRepository.findVisaByType(request.getDescription());
	// List<Report90DayTypeResponseType> response = new ArrayList<>();
	// for (Report90DayTypeResponseType visaTypeResponseDTO : visaList) {
	// VisaTypeResponseDTO dto = new VisaTypeResponseDTO();
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

	public ResponseEntity<?> getAllReport90DayVisaTypes() {
		try {
			List<Report90DayVisaType> vs = vsTypeRepo.findAllReport90DayVisaTypes();

			return ResponseEntity.status(HttpStatus.OK).body(vs);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving visa type: " + e.getMessage());
		}
	}

	@Transactional
	public ResponseEntity<Report90DayVisaType> getEmbassyLetteById(int id) {
		Optional<Report90DayVisaType> visaTypeOpt = vsTypeRepo.findById(id);
		if (!visaTypeOpt.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if not found
		}

		return new ResponseEntity<>(visaTypeOpt.get(), HttpStatus.OK); // Return the found VisaExtensionType
	}

	// @Transactional
	// public List<Report90DayTypeResponseType> getAllVisaType() {
	// List<Report90DayTypeResponseType> visaList =
	// vsiaTypeRepository.findAllVisaType();
	// List<Report90DayTypeResponseType> response = new ArrayList<>();
	// for (Report90DayTypeResponseType visaTypeResponseDTO : visaList) {
	// VisaTypeResponseDTO dto = new VisaTypeResponseDTO();
	// Report90DayVisaType type = new Report90DayVisaType();
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

	@Transactional
	public ResponseEntity<String> updateVisaType(int id, Report90DayTypeRequestDTO request) {
		Optional<Report90DayVisaType> getVisa = vsiaTypeRepository.findById(id);
		if (!getVisa.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			Report90DayVisaType visaType = getVisa.get();
			visaType.setDescription(request.getDescription());
			visaType.setUpdatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
			visaType.setUpdatedDate(new Date());
			visaType.setPrice(request.getPrice());
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

			// Delete VisaType
			vsiaTypeRepository.deleteVisaTypeById(id);

			return new ResponseEntity<>("VisaType deleted successfully.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred while deleting VisaType: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	public List<Report90DayVisaType> getAllVisaType() {
		return vsiaTypeRepository.getAllVisa();
	}

}
