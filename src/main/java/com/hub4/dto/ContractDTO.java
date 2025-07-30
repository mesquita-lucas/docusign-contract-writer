package com.hub4.dto;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

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
        String prodSellValue,
        List<ImageDTO> images
){}
