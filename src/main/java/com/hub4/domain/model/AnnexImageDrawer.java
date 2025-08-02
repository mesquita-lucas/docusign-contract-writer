package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;

    public AnnexImageDrawer(PDDocument document) {
        this.document = document;
    }

    public void draw(List<ImageDTO> images){
        PDFImageRenderer renderer = new PDFImageRenderer(document, 3);

        for (ImageDTO image : images) {
            if (renderer.isFull()) {
                renderer.setPageIndex(4);
            }

            renderer.addImage(image);
        }
    }
}
