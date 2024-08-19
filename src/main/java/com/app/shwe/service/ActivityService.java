package com.app.shwe.service;

import com.app.shwe.model.Activity;
import com.app.shwe.repository.ActivityRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    public Page<Activity> getAllActivities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return activityRepository.findAll(pageable);
    }
}
