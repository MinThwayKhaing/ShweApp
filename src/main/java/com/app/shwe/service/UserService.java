package com.app.shwe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.model.Report;
import com.app.shwe.model.User;
import com.app.shwe.userRepository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	public List<UserReportDTO> getReportByUserId(String id) {
		List<UserReportDTO> dtoList = userRepo.findUserContentById(id);

		List<UserReportDTO> result = new ArrayList<>();

		for (UserReportDTO dto : dtoList) {
			UserReportDTO userReportDTO = new UserReportDTO();
			User user = new User();
			user.setUserName(dto.getUsername());
			userReportDTO.setUsername(dto.getUsername());
			Report report = new Report();
			report.setContent(dto.getReport());
			userReportDTO.setReport(dto.getReport());
			result.add(userReportDTO);
		}

		return result;
	}
	

}
