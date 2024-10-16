package com.app.shwe.service;

import com.app.shwe.model.Activity;
import com.app.shwe.repository.ActivityRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserRepository userRepository;

    public Activity saveActivity(Activity activity) {
        activity.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
        activity.setCreatedDate(new Date());
        activity.setUpdatedDate(new Date());
        return activityRepository.save(activity);
    }

    public Optional<Activity> getActivityById(int id) {
        return activityRepository.findById(id);
    }

    public Activity updateActivity(int id, Activity updatedActivity) {
        return activityRepository.findById(id).map(activity -> {
            activity.setName(updatedActivity.getName());
            activity.setArticles(updatedActivity.getArticles());
            return activityRepository.save(activity);
        }).orElseThrow(() -> new RuntimeException("Activity not found with id " + id));
    }

    public void deleteActivity(int id) {
        activityRepository.deleteById(id);
    }

    public ResponseEntity<?> getAllActivities(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.status(HttpStatus.OK).body(activityRepository.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while selecting Activities: " + e.getMessage());
        }

    }
    
    public List<Activity> getAllActivityList(){
    	List<Activity> activityList = activityRepository.getAllActivityList();
    	List<Activity> ac = new ArrayList<>();
    	for (Activity activity : activityList) {
			ac.add(activity);
		}
    	return ac;
    }
}
