package com.app.shwe.controller;

import com.app.shwe.dto.RatingRequest;
import com.app.shwe.model.CarRent;
import com.app.shwe.model.Rating;
import com.app.shwe.model.Translator;
import com.app.shwe.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable int id) {
        Rating rating = ratingService.getRatingById(id).orElseThrow(() -> new RuntimeException("Rating not found"));
        return ResponseEntity.ok(rating);
    }

    @PostMapping("/car")
    public ResponseEntity<?> createRatingCar(@RequestBody RatingRequest rating) {
        return ratingService.createRatingForCar(rating);
    }

    @PostMapping("/translator")
    public ResponseEntity<?> createRatingTranslator(@RequestBody RatingRequest rating) {
        return ratingService.createRatingForTranslator(rating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable int id, @RequestBody Rating ratingDetails) {
        Rating updatedRating = ratingService.updateRating(id, ratingDetails);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable int id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/high-rated-cars")
    public ResponseEntity<?> getHighRatedCars(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ratingService.getHighRatedCars(pageable);

    }

    @GetMapping("/high-rated-translators")
    public ResponseEntity<?> getHighRatedTranslator(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ratingService.getHighRatedTranslators(pageable);

    }

    @GetMapping("/car/{carId}")
    public ResponseEntity<?> getCarByIdWithRating(@PathVariable int carId) {
        return ratingService.getCarRentByIdWithRating(carId);

    }

    @GetMapping("/translator/{translatorId}")
    public ResponseEntity<?> getTranslatorByIdWithRating(@PathVariable int translatorId) {
        return ratingService.getTranslatorByIdWithRating(translatorId);

    }
}
