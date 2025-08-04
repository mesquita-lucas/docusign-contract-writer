package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

import java.io.IOException;
import java.util.List;

public class AnnexImageDrawer {
    private final PDDocument document;

    public AnnexImageDrawer(PDDocument document) {
        this.document = document;
    }

    public void draw(List<ImageDTO> images){
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
        } catch (IOException e){
            System.out.println("Error while drawing images: " + e.getMessage());
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
}
