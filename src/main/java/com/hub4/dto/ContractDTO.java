package com.hub4.dto;

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
        String prodSellValue
){}
