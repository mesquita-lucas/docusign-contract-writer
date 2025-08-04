package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.text.Position;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class PDFImageRenderer {
    private final int CONTAINER_HEIGHT = 609;
    private final int CONTAINER_WIDTH = 385;
    private final int CELL_HEIGHT = CONTAINER_HEIGHT / 3;

    private int numberOfImagesAdded;

    private final PDDocument document;
    private int pageIndex;

    public PDFImageRenderer(PDDocument document, int pageIndex) {
        this.document = document;
        this.numberOfImagesAdded = 0;
        this.pageIndex = pageIndex;
    }

    public void addImage(ImageDTO image) {
        try (
                PDPageContentStream cs = new PDPageContentStream(
                document,
                document.getPage(pageIndex),
                AppendMode.APPEND,
                true
        )) {
            byte[] imageInBytes = Base64.getDecoder().decode(image.imageBase64());

            PDImageXObject imageXObject = PDImageXObject.createFromByteArray(
                    document,
                    imageInBytes,
                    image.imageName()
            );

            System.out.println("Image Loaded!");

            Map<String, Float> newDimensions = scale(imageXObject);
            Position whereToDraw = getDrawingPosition(newDimensions);

            System.out.println("Scaling done. Drawing image");

            cs.drawImage(
                    imageXObject,
                    whereToDraw.getX(),
                    whereToDraw.getY(),
                    newDimensions.get("width"),
                    newDimensions.get("height")
            );

            System.out.println("Image Drawn!");

            numberOfImagesAdded++;
        } catch (IOException e) {
            System.out.println("Erro ao desenhar imagem: " + e.getMessage());
        }
    }

    public void setPageIndex(int pageIndex){
        this.pageIndex = pageIndex;
        this.numberOfImagesAdded = 0;
    }
    private Position getDrawingPosition(Map<String, Float> newDimensions) {
        float imageWidth = newDimensions.get("width");
        float cellMiddlePoint = (float) (CONTAINER_WIDTH + 140) / 2; //margins are 70 each
        int containerTopPosition = 692;

        int yValue = containerTopPosition - (CELL_HEIGHT * numberOfImagesAdded);

        return new Position(cellMiddlePoint - (imageWidth / 2), yValue);
    }

    private Map<String, Float> scale(PDImageXObject image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        float scaleFactor = Math.min(CONTAINER_WIDTH / imageWidth, CELL_HEIGHT / imageHeight);

        return Map.of(
                "width", imageWidth * scaleFactor,
                "height", imageHeight * scaleFactor
        );
    }

    public boolean isFull() {
        return numberOfImagesAdded >= 3;
    }
}
