package com.hub4.domain.utils;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.text.Position;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class PDFImageRenderer {
    private final float CONTAINER_HEIGHT = 609f;
    private final float CONTAINER_WIDTH = 385f;
    private final float CELL_HEIGHT = CONTAINER_HEIGHT / 3;

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

        System.out.println("Image Drawn and saved!");

        numberOfImagesAdded++;
    }

    public void resetImageCount(){
        this.numberOfImagesAdded = 0;
    }

    private Position getDrawingPosition(Map<String, Float> newDimensions) {
        float imageWidth = newDimensions.get("width");
        final float PAGE_MARGIN_X = 70f;
        float cellMiddlePoint = PAGE_MARGIN_X + (CONTAINER_WIDTH / 2);
        int containerTopPosition = 692;

        final float IMAGE_MARGIN = 15f;
        float yValue = (containerTopPosition - CELL_HEIGHT)
                - (CELL_HEIGHT * numberOfImagesAdded)
                - (IMAGE_MARGIN * numberOfImagesAdded);

        return new Position(cellMiddlePoint - (imageWidth / 2) + 35, yValue);
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
