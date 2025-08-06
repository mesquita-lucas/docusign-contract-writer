package com.hub4.domain.utils;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

import java.io.*;
import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;
    private final List<ImageDTO> images;

    public AnnexImageDrawer(byte[] pdf, List<ImageDTO> images) throws IOException {
        this.document = PDDocument.load(pdf);
        this.images = images;
    }

    public void draw(){
        if (images == null || images.isEmpty()) return;

        PDFImageRenderer renderer = new PDFImageRenderer(document);
        PDPageContentStream contentStream = null;

        int currentPageIndex = 3;

        try
        {
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
                try
                {
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
}

