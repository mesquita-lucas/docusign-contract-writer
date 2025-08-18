package com.hub4.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ContractDTO(
        String consignorName,
        String consignorCPF,
        String consignorEmail,
        String prodType,
        String prodBrand,
        String prodModel,
        String prodYear,
        String conservationState,
        String prodAccessories,
        BigDecimal prodSellValue,
        List<ImageDTO> images
){}
