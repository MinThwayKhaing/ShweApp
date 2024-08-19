package com.app.shwe.service;

import com.app.shwe.dto.CarRentRatingDTO;
import com.app.shwe.dto.RatingRequest;
import com.app.shwe.dto.TranslatorRatingDTO;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.Rating;
import com.app.shwe.model.Translator;
import com.app.shwe.model.User;
import com.app.shwe.repository.CarRentRepository;
import com.app.shwe.repository.RatingRepository;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CarRentRepository carRentRepository;

    @Autowired
    private TranslatorRepository translatorRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Optional<Rating> getRatingById(int id) {
        return ratingRepository.findById(id);
    }

    public ResponseEntity<?> createRatingForCar(RatingRequest dto) {
        if (dto.getCarRentId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Car rent id can't be null or zero");
        }
        try {
            Rating model = new Rating();
            int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
            Optional<User> user = userRepo.findById(userId);
            Optional<CarRent> car = carRentRepository.findById(dto.getCarRentId());

            model.setUser(user.get());
            model.setCarRent(car.get());
            model.setRating(dto.getRating());
            model.setDescription(dto.getDescription());
            model.setCreatedDate(new Date());
            model.setUpdatedDate(new Date());
            ratingRepository.save(model);
            return ResponseEntity.status(HttpStatus.OK).body("Review  saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving Review: " + e.getMessage());
        }
    }

    public ResponseEntity<?> createRatingForTranslator(RatingRequest dto) {

        if (dto.getTranslatorId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Translator id can't be null or zero");
        }
        try {
            Rating model = new Rating();
            int userId = userRepo.authUser(SecurityUtils.getCurrentUsername());
            Optional<User> user = userRepo.findById(userId);
            Optional<Translator> translator = translatorRepository.findById(dto.getTranslatorId());
            model.setUser(user.get());
            model.setTranslator(translator.get());
            model.setRating(dto.getRating());
            model.setDescription(dto.getDescription());
            model.setCreatedDate(new Date());
            model.setUpdatedDate(new Date());

            ratingRepository.save(model);

            return ResponseEntity.status(HttpStatus.OK).body("Review  saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while saving Review: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getHighRatedCars(Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carRentRepository.findAllWithHighRating(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while selecting Review: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getHighRatedTranslators(Pageable pageable) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(translatorRepository.findAllWithHighRating(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while selecting Review: " + e.getMessage());
        }
    }

    public Rating updateRating(int id, Rating ratingDetails) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new RuntimeException("Rating not found"));

        rating.setRating(ratingDetails.getRating());
        rating.setDescription(ratingDetails.getDescription());

        return ratingRepository.save(rating);
    }

    public void deleteRating(int id) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new RuntimeException("Rating not found"));
        ratingRepository.delete(rating);
    }

    public ResponseEntity<?> getCarRentByIdWithRating(int carId) {

        try {
            Optional<CarRentRatingDTO> car = carRentRepository.findByIdWithRating(carId);
            if (!car.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while Car Rent Review is not Present ");
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(car);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while Car Rent Review is not Present ");
        }

    }

    public ResponseEntity<?> getTranslatorByIdWithRating(int translatorId) {
        try {
            Optional<TranslatorRatingDTO> tran = translatorRepository.findByIdWithRating(translatorId);

            if (!tran.isPresent()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred while Car Rent Review is not Present ");
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(tran);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while Translator Review is not Present ");
        }

    }
}