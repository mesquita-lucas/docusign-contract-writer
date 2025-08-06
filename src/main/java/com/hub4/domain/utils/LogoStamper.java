package com.hub4.domain.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.text.Position;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class LogoStamper {
    private final PDDocument document;
    private final PDImageXObject logo;

    public LogoStamper(byte[] pdf) throws IOException {
        this.document = PDDocument.load(pdf);
        this.logo = loadLogo(document);
    }

    public void stamp() throws IOException {
        final int numberOfPages = document.getNumberOfPages() - 2;
        final Position logoPosition = new Position(30, 765);
        final int logoSideSize = 50;

        for (int i = 0; i < numberOfPages; i++) {
            try (
                    PDPageContentStream cs = new PDPageContentStream(
                            document,
                            document.getPage(i),
                            PDPageContentStream.AppendMode.APPEND,
                            true
                    )
            ){
                cs.drawImage(logo, logoPosition.getX(), logoPosition.getY(), logoSideSize, logoSideSize);
            }
        }
    }

    public byte[] save() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            document.save(baos);
            document.close();

            return baos.toByteArray();
        }
    }

    private PDImageXObject loadLogo(PDDocument document) throws IOException {
        final String logoPath = "HUB4-preset-logos-15.jpg";

        try(InputStream is = LogoStamper.class.getClassLoader().getResourceAsStream(logoPath)) {
            Objects.requireNonNull(is, "InputStream is null");
            byte[] imageInBytes = is.readAllBytes();

            return PDImageXObject.createFromByteArray(document, imageInBytes, null);
        }
    }
}
