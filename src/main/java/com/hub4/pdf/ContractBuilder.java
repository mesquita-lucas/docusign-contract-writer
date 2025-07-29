package com.hub4.pdf;
import com.hub4.dao.JSONContentDAO;
import com.hub4.dao.MockContractDAO;
import com.hub4.dto.ContractDTO;
import com.hub4.model.ContractContents;
import com.hub4.model.Section;
import com.hub4.model.SectionType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.ImageElement;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Position;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class ContractBuilder {
    private final static String logoPath = "HUB4-preset-logos-15.jpg";
    private final static int logoSideSize = 50;
    private final static Position logoPosition = new Position(30, 785);

    private final static BaseFont fontFamily = BaseFont.Helvetica;
    private final static int fontSize = 11;

    private ContractBuilder() {}

    public static byte[] build() throws IOException {
        Document document = new Document(70, 70, 100, 50);

        final ContractContents content = JSONContentDAO.get();

        List<Section> sections = createSectionList(content);
        sections.forEach(section -> addSection(document, section));

        try{
            ByteArrayOutputStream logolessDocument = new ByteArrayOutputStream();
            document.save(logolessDocument);

            byte[] finalContract = addHeaderLogo(logolessDocument.toByteArray());

            return finalContract;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private static byte[] addHeaderLogo(byte[] documentInBytes) throws IOException {
        PDDocument logolessDocument = PDDocument.load(documentInBytes);
        int numberOfPages = logolessDocument.getNumberOfPages();
        PDImageXObject logo = loadImage(logolessDocument);

        for (int i = 0; i < numberOfPages; i++) {
            try(PDPageContentStream cs = new PDPageContentStream(
                    logolessDocument,
                    logolessDocument.getPage(i),
                    true,
                    true))
            {
                cs.drawImage(logo, logoPosition.getX(), logoPosition.getY(), logoSideSize, logoSideSize);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        logolessDocument.save(baos);
        return baos.toByteArray();
    }

    private static PDImageXObject loadImage(PDDocument document) throws IOException {
        try(InputStream image = ContractBuilder.class.getClassLoader().getResourceAsStream(logoPath)){
            Objects.requireNonNull(image, "Image cannot be null");

            PDImageXObject loadedImage = PDImageXObject.createFromByteArray(document, image.readAllBytes(), null);

            return loadedImage;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private static void addSection(Document doc, Section section){
        Paragraph par = new Paragraph();

        try{

            if(section.getSectionType().equals(SectionType.SIGNATURE)){
                par.addText(section.getContent(), section.getFontSize(), PDType1Font.HELVETICA);
            } else {

                if(section.getSectionType().equals(SectionType.ANNEX)) {
                    doc.add(ControlElement.NEWPAGE);
                }

                par.addMarkup(section.getContent(), section.getFontSize(), section.getBaseFont());
            }

            par.setAlignment(section.getAlignment());
            par.setLineSpacing(1.8f);

            Paragraph paragraph = new Paragraph();

            doc.add(par);
            doc.add(blankLine());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Paragraph blankLine() throws IOException {
        Paragraph par = new Paragraph();
        par.addText(" ", 11, PDType1Font.HELVETICA);

        return par;
    }

    private static List<Section> createSectionList(ContractContents content){
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
}
