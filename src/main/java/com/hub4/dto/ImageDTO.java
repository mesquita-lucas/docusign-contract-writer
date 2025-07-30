package com.hub4.dto;

public record ImageDTO(
        String imageBase64,
        String imageMimeType,
        String imageName
) {
}
