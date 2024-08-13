package com.app.shwe.service;

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

import com.app.shwe.dto.PriceRequestDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.Tm30RequestDTO;
import com.app.shwe.model.Price;
import com.app.shwe.model.Tm30;
import com.app.shwe.model.User;
import com.app.shwe.model.VisaServices;
import com.app.shwe.repository.PriceRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class PriceService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PriceRepository priceRepository;

    @Transactional
	public ResponseEntity<String> savePriec(PriceRequestDTO request) {
		if (request == null) {
			throw new IllegalArgumentException("Request and required fields must not be null");
		}
		try {
			Price price = new Price();
            price.setDescription(request.getDescription());
            price.setType(request.getType());
            price.setPrice(request.getPrice());
            price.setCreatedBy(userRepo.authUser(SecurityUtils.getCurrentUsername()));
            price.setCreatedDate(new Date());
            priceRepository.save(price);
			return ResponseEntity.status(HttpStatus.OK).body("Tm-30 saved successfully.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving TM-30: " + e.getMessage());
		}
	}

    @Transactional
	public Optional<Price> getPriceById(Integer id) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null");
		}
		Optional<Price> price = priceRepository.findById(id);
		return price;
	}

    @Transactional
	public Page<Price> getAllPrice(SearchDTO search) {
		int page = (search.getPage() < 1) ? 0 : search.getPage() - 1;
		int size = search.getSize();
		Pageable pageable = PageRequest.of(page, size);
		return priceRepository.getAllPrice(pageable);
	}

    @Transactional
	public ResponseEntity<String> updatePrice(int id,PriceRequestDTO request) {
		Optional<Price> getPrice = priceRepository.findById(id);
		if (!getPrice.isPresent()) {
			throw new IllegalArgumentException("ID not found");
		}

		try {
			Price price = getPrice.get();
            price.setDescription(request.getDescription());
            price.setType(request.getType());
            price.setPrice(request.getPrice());
            price.setUpdatedBy(userRepo.authUser(SecurityUtils.getCurrentUsername()));
            price.setUpdatedDate(new Date());
            priceRepository.save(price);
			return new ResponseEntity<>("Price updated successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

    @Transactional
	public ResponseEntity<?> deletePirceById(int id) {
		int count = priceRepository.checkPriceById(id);

		if (count == 0) {
			return new ResponseEntity<>("Price with ID " + id + " not found", HttpStatus.NOT_FOUND);
		}

		try {
			priceRepository.deleteById(id);
			return new ResponseEntity<>("Price deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
}
