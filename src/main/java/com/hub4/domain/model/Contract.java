package com.hub4.domain.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Position;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class Contract {
    private byte[] documentInBytes;
    private final Document document;
    private final List<Section> sections;

    public Contract(ContractContents contractContents) {
        this.document = new Document(70, 70, 100, 50);
        this.sections = loadSectionsList(contractContents);
    }

    public void writeContract(){
        sections.forEach(section -> {
            try {
                write(section);
            } catch (IOException e) {
                System.out.println("Unable to write contract to file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });

        try {
            save(document);
        } catch (IOException e) {
            System.out.println("Unable to save document after writing content: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void addHeader() throws IOException {
        final int logoSideSize = 50;
        final String logoPath = "HUB4-preset-logos-15.jpg";
        final Position logoPosition = new Position(30, 775);

        try(
                InputStream is = Contract.class.getClassLoader().getResourceAsStream(logoPath);
                PDDocument document = PDDocument.load(documentInBytes)
        ){
            Objects.requireNonNull(is, "Unable to add image to inputStream");
            int numberOfPages = document.getNumberOfPages();

            String base64Logo = Base64.getEncoder().encodeToString(is.readAllBytes());
            PDImageXObject logo = loadImage(base64Logo, document);

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

            save(document);
        }
    }

    /*public void addAnnexImages(List<ImageDTO> images){

    }
     */

    public byte[] saveContract(){
        return documentInBytes;
    }

    private PDImageXObject loadImage(String imageBase64, PDDocument document) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        return PDImageXObject.createFromByteArray(document, imageBytes, null);
    }

    private void save(Document document) throws IOException {
        this.documentInBytes = toByteArray(document::save);
    }

    private void save(PDDocument document) throws IOException {
        this.documentInBytes = toByteArray(document::save);
    }

    private byte[] toByteArray(Savable savable) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
        {
            savable.save(baos);
            return baos.toByteArray();
        }
    }

    private List<Section> loadSectionsList(ContractContents content){
        final BaseFont fontFamily = BaseFont.Helvetica;
        final int fontSize = 11;

        return List.of(
                new Section(SectionType.TITLE, content.contractTitle(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.consignorID(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.consigneeID(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.contractObject(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.productDetails(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.documentation(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.transport(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.truthfulnessDeclaration(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.nonApparentDamageDisclaimer(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.exclusiveSale(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.insurance(), fontFamily, fontSize),
                new Section(SectionType.TEXT, content.generalProvisions(), fontFamily, fontSize),
                new Section(SectionType.SIGNATURE, content.signatures(), fontFamily, fontSize),
                new Section(SectionType.ANNEX, content.annex(), fontFamily, fontSize)
        );
    }

    private void write(Section section) throws IOException {
        Paragraph par = new Paragraph();

        if(section.getSectionType().equals(SectionType.SIGNATURE)){
            par.addText(section.getContent(), section.getFontSize(), PDType1Font.HELVETICA);
        } else {

            if(section.getSectionType().equals(SectionType.ANNEX)) {
                document.add(ControlElement.NEWPAGE);
            }

            par.addMarkup(section.getContent(), section.getFontSize(), section.getBaseFont());
        }

        par.setAlignment(section.getAlignment());
        par.setLineSpacing(1.8f);

        document.add(par);
        document.add(blankLine());
    }

    private Paragraph blankLine() throws IOException {
        Paragraph par = new Paragraph();
        par.addText(" ", 11, PDType1Font.HELVETICA);

        return par;
    }
}
