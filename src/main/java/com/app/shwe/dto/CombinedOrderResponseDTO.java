package com.app.shwe.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class CombinedOrderResponseDTO {
    private Page<CarOrderResponseDTO> carOrders;
    private Page<TranslatorOrderResponseDTO> translatorOrders;
}