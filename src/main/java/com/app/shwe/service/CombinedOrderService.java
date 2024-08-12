package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.CombinedOrderResponseDTO;
import com.app.shwe.dto.SearchDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.repository.UserRepository;
import com.app.shwe.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class CombinedOrderService {
	
	@Autowired
    private CarOrderService carOrderService;

    @Autowired
    private TranslatorService translatorOrderService;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CombinedOrderResponseDTO getCombinedOrders(SearchDTO dto) {
        Page<CarOrderResponseDTO> carOrdersPage = carOrderService.showCarOrders(dto);
        Page<TranslatorOrderResponseDTO> translatorOrdersPage = translatorOrderService.searchHireTranslator(dto);

        CombinedOrderResponseDTO combinedResponse = new CombinedOrderResponseDTO();
        combinedResponse.setCarOrders(carOrdersPage);
        combinedResponse.setTranslatorOrders(translatorOrdersPage);

        return combinedResponse;
    }
    
    @Transactional
    public CombinedOrderResponseDTO findOrderByUserId(SearchDTO dto) {
    	int userId = userRepository.authUser(SecurityUtils.getCurrentUsername());
        Page<CarOrderResponseDTO> carOrdersPage = carOrderService.findOrderByUserId(userId,dto);
        Page<TranslatorOrderResponseDTO> translatorOrdersPage = translatorOrderService.findOrderByUserId(userId,dto);

        CombinedOrderResponseDTO combinedResponse = new CombinedOrderResponseDTO();
        combinedResponse.setCarOrders(carOrdersPage);
        combinedResponse.setTranslatorOrders(translatorOrdersPage);

        return combinedResponse;
    }

}
