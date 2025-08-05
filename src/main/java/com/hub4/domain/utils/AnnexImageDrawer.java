package com.hub4.domain.utils;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.util.Base64;
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

                saveImageForDebugging(image, currentPageIndex);

                renderer.drawImageOnStream(contentStream, image);
            }
        } catch (Exception e){
            System.out.println("Erro ao desenhar imagem no PDF: " + e.getMessage());
            e.printStackTrace();
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
            return baos.toByteArray();
        }
    }

    private void saveImageForDebugging(ImageDTO image, int index) {
        try {
            // Garante que a pasta de debug exista
            File debugDir = new File("debug_images");
            if (!debugDir.exists()) {
                debugDir.mkdirs();
            }

            // Decodifica a string Base64 para bytes
            byte[] imageInBytes = Base64.getDecoder().decode(image.imageBase64());

            // Cria um nome de arquivo único para evitar sobreposições
            String fileName = index + "_" + image.imageName();
            File outputFile = new File(debugDir, fileName);

            // Usa try-with-resources para garantir que o FileOutputStream seja fechado
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(imageInBytes);
            }
            System.out.println("Imagem de debug salva em: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("ERRO ao salvar imagem de debug '" + image.imageName() + "': " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Isso acontece se a string Base64 for inválida
            System.err.println("ERRO: A string Base64 para a imagem '" + image.imageName() + "' é inválida. " + e.getMessage());
        }
    }
}
