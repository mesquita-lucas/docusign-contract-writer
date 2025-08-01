package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

import java.io.IOException;
import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;

    public AnnexImageDrawer(PDDocument document) {
        this.document = document;
    }

    public void draw(List<ImageDTO> images) throws IOException {
        PDPageContentStream cs = new PDPageContentStream(
                document,
                document.getPage(3),
                AppendMode.APPEND,
                true
        );

        ImageContainer container = new ImageContainer(document, cs);

        for (ImageDTO image : images) {
            if (container.isFull()) {
                cs.close();

                PDPageContentStream newCs = new PDPageContentStream(
                        document,
                        document.getPage(4),
                        AppendMode.APPEND,
                        true);

                container = new ImageContainer(document, newCs);
            }

            container.addImage(image);
        }

        cs.close();
    }
}
