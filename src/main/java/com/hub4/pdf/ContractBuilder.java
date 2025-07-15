package com.hub4.pdf;
import com.hub4.dao.JSONContentDAO;
import com.hub4.model.ContractContents;
import com.hub4.model.Section;
import com.hub4.model.SectionType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.ImageElement;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Position;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ContractBuilder {
    private final static String logoPath = "HUB4-preset-logos-15.png";
    private final static int logoSideSize = 50;
    private final static Position logoPosition = new Position(30, 815);

    private final static BaseFont fontFamily = BaseFont.Helvetica;
    private final static int fontSize = 11;

    private final static String fileOutputPath = "temp/build.pdf";

    private final static ContractContents content = JSONContentDAO.get();

    private ContractBuilder() {}

    public static void build(){
        Document document = new Document(70, 70, 100, 50);

        List<Section> sections = createSectionList(content);

        sections.forEach(section -> addSection(document, section));

        try{
            document.save(new FileOutputStream(fileOutputPath));
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        try(InputStream is = new FileInputStream(fileOutputPath)){
            PDDocument finalDoc = PDDocument.load(is);

            addHeaderLogo(finalDoc, finalDoc.getPage(0));

            finalDoc.save(new FileOutputStream(fileOutputPath));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void addHeaderLogo(PDDocument doc, PDPage page){
        try( PDPageContentStream cs = new PDPageContentStream(doc, page, true, true) ){
            ImageElement image = loadImage(doc);
            image.draw(doc, cs, image.getAbsolutePosition(), null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static ImageElement loadImage(PDDocument doc) {
        try(InputStream image = ContractBuilder.class.getClassLoader().getResourceAsStream(logoPath)){
            Objects.requireNonNull(image, "Image cannot be null");

            ImageElement logo = new ImageElement(image);
            logo.setHeight(logoSideSize);
            logo.setWidth(logoSideSize);

            logo.setAbsolutePosition(logoPosition);

            return logo;
        } catch (Exception e) {
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
