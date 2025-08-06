package com.hub4.domain.utils;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;
    private final List<ImageDTO> images;

    public AnnexImageDrawer(List<ImageDTO> images) throws IOException {
        this.images = images;
        this.document = createAnnexDocument();
    }

    public void draw() {
        if (images == null || images.isEmpty()) return;

        PDFImageRenderer renderer = new PDFImageRenderer(document);
        PDPageContentStream contentStream = null;

        int currentPageIndex = 0;

        try {
            for(ImageDTO image : images){
                if(renderer.isFull()){
                    if(contentStream != null){
                        contentStream.close();
                    }

                    renderer.resetImageCount();
                    currentPageIndex++;
                    contentStream = null;
                }

                if (contentStream == null) {
                    contentStream = new PDPageContentStream(
                            document,
                            document.getPage(currentPageIndex),
                            AppendMode.APPEND,
                            true
                    );
                }

                renderer.drawImageOnStream(contentStream, image);
            }
        } catch (Exception e){
            System.out.println("Erro ao desenhar imagem no PDF: " + e.getMessage());
        } finally {
            if (contentStream != null){
                try {
                    contentStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
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

    private PDDocument createAnnexDocument() throws IOException {
        int numberOfImages = images.size();
        PDDocument annexDocument = new PDDocument();

        if (numberOfImages > 3) {
            createAnnexPage(annexDocument);
        }

        createAnnexPage(annexDocument);

        return annexDocument;
    }

    private void createAnnexPage(PDDocument document) throws IOException {
        final String title = "ANEXO - IMAGENS DO PRODUTO";

        PDPage page = new PDPage();
        document.addPage(page);

        try(PDPageContentStream cs = new PDPageContentStream(
                document,
                page,
                AppendMode.APPEND,
                true
        )){
            cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
            cs.beginText();
            cs.newLineAtOffset(225,650);
            cs.showText(title);
            cs.endText();
        }
    }
}
