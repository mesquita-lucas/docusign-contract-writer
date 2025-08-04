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

    public PDFImageRenderer(PDDocument document) {
        this.document = document;
        this.numberOfImagesAdded = 0;
    }

    public void drawImageOnStream(PDPageContentStream cs, ImageDTO image) throws IOException {
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
    }

    public void resetImageCount(){
        this.numberOfImagesAdded = 0;
    }

    private Position getDrawingPosition(Map<String, Float> newDimensions) {
        float imageWidth = newDimensions.get("width");
        float cellMiddlePoint = (float) (CONTAINER_WIDTH + 140) / 2; // margins are 70 each
        int containerTopPosition = 692;

        int yValue = (containerTopPosition - CELL_HEIGHT) - (CELL_HEIGHT * numberOfImagesAdded);

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
