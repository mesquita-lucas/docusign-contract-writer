package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;
    private ImageContainer container;

    public AnnexImageDrawer(PDDocument document) {
        this.document = document;
    }

    public void draw(List<ImageDTO> images) {
        for (ImageDTO image : images) {
            if (container == null || container.isFull()) {
                this.container = new ImageContainer(document, document.getPage(4));
            }

            container.addImage(image);
        }
    }
}
