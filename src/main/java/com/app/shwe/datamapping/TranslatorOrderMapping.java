package com.app.shwe.datamapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.TranslatorOrderRequestDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.dto.TranslatorRequestDTO;
import com.app.shwe.model.MainOrder;
import com.app.shwe.model.Translator;
import com.app.shwe.model.TranslatorOrder;
import com.app.shwe.model.User;
import com.app.shwe.repository.MainOrderRepository;
import com.app.shwe.repository.TranslatorOrderRepostitory;
import com.app.shwe.repository.TranslatorRepository;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.service.TranslatorOrderIdGenerator;
import com.app.shwe.utils.SecurityUtils;

@Service
public class TranslatorOrderMapping {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TranslatorRepository translatorRepository;

	@Autowired
	private TranslatorOrderIdGenerator idGenerator;

	@Autowired
	private MainOrderRepository mainOrderRepository;

	public TranslatorOrder mapToTranslatorOrder(TranslatorRequestDTO dto) {
		TranslatorOrder order = new TranslatorOrder();
		Translator translator = translatorRepository.findById(dto.getTranslator_id())
				.orElseThrow(() -> new RuntimeException("CarRent not found for ID: " + dto.getTranslator_id()));
		order.setStatus("Pending");
		order.setCreatedBy(userRepository.authUser(SecurityUtils.getCurrentUsername()));
		order.setCreatedDate(new Date());
		order.setFromDate(dto.getFromDate());
		order.setToDate(dto.getToDate());
		order.setMeetingDate(dto.getMeetingDate());
		order.setMeetingPoint(dto.getMeetingPoint());
		order.setMeetingTime(dto.getMeetingTime());
		order.setPhoneNumber(dto.getPhoneNumber());
		order.setUsedFor(dto.getUsedFor());
		order.setTranslator(translator);
		order.setSysKey(idGenerator.generateNextCarOrderId());

		return order;
	}

	public TranslatorOrder mapToTranslatorOrder(TranslatorOrderRequestDTO dto) {
		TranslatorOrder order = new TranslatorOrder();
		MainOrder mainOrder = new MainOrder();

		int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
		order.setFromDate(dto.getFromDate());
		order.setToDate(dto.getToDate());
		order.setUsedFor(dto.getUsedFor());
		order.setMeetingPoint(dto.getMeetingPoint());
		order.setMeetingDate(dto.getMeetingDate());
		order.setMeetingTime(dto.getMeetingTime());
		order.setPhoneNumber(dto.getPhoneNumber());
		order.setStatus("Pending");
		order.setCreatedBy(userId);
		order.setCreatedDate(new Date());

		order.setSysKey(idGenerator.generateNextCarOrderId());
		mainOrder.setStart_date(order.getFromDate());
		mainOrder.setEnd_date(order.getToDate());
		mainOrder.setCreatedBy(userId);
		mainOrder.setCreatedDate(new Date());
		mainOrder.setSys_key(order.getSysKey());
		mainOrder.setOrder_id(order.getId());
		mainOrder.setStatus(order.getStatus());
		mainOrder.setUser(user);
		mainOrderRepository.save(mainOrder);
		return order;
	}

}
