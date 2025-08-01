package com.hub4.api.dto;

public record ImageDTO(
        String imageBase64,
        String imageMimeType,
        String imageName
) {
}
