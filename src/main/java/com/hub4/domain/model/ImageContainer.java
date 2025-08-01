package com.hub4.domain.model;

import com.hub4.api.dto.ImageDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.text.Position;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class ImageContainer {
    private final int containerHeight = 609;
    private final int containerWidth = 385;

    int cellHeight = containerHeight / 3;

    private int numberOfImagesAdded;

    private final PDDocument document;
    private final PDPage page;

    public ImageContainer(PDDocument document, PDPage page) {
        this.document = document;
        this.page = page;
        this.numberOfImagesAdded = 0;
    }

    public void addImage(ImageDTO image) {
        try(PDPageContentStream cs = new PDPageContentStream(
                document,
                page,
                PDPageContentStream.AppendMode.APPEND,
                true
        )){
            byte[] imageInBytes = Base64.getDecoder().decode(image.imageBase64());

            PDImageXObject imageXObject = PDImageXObject.createFromByteArray(
                    document,
                    imageInBytes,
                    image.imageName()
            );

            Map<String, Float> newDimensions = scale(imageXObject);
            Position whereToDraw = getDrawingPosition(newDimensions); //always in the center

            cs.drawImage(
                    imageXObject,
                    whereToDraw.getX(),
                    whereToDraw.getY(),
                    newDimensions.get("width"),
                    newDimensions.get("height")
            );

            numberOfImagesAdded++;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Position getDrawingPosition(Map<String, Float> newDimensions) {
        float imageWidth = newDimensions.get("width");
        float cellMiddlePoint = (float) (containerWidth + 140) / 2; //margins are 70 each
        int yValue = (692 - 203) - (203 * numberOfImagesAdded); //starting point, them populate other cells

        return new Position(cellMiddlePoint - (imageWidth / 2), yValue);
    }

    private Map<String, Float> scale(PDImageXObject image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        float scaleFactor = Math.min(containerWidth / imageWidth, cellHeight / imageHeight);

        return Map.of(
                "width", imageWidth * scaleFactor,
                "height", imageHeight * scaleFactor
        );
    }

    public boolean isFull() {
        return numberOfImagesAdded >= 3;
    }
}
