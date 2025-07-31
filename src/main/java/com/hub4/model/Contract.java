package com.hub4.model;

import com.hub4.dto.ContractDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Position;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Contract {
    private final ContractDTO contractDTO;
    private final ContractContents contractContents;
    private byte[] documentInBytes;
    private Document document;
    private final List<Section> sections;

    private final String logoPath = "HUB4-preset-logos-15.jpg";
    private final int logoSideSize = 50;
    private final Position logoPosition = new Position(30, 775);

    public Contract(ContractDTO contractDTO, ContractContents contractContents) {
        this.contractDTO = contractDTO;
        this.contractContents = contractContents;

        this.document = new Document(70, 70, 100, 50);
        this.sections = loadSectionsList(contractContents);
    }

    //todo: ideia para fazer dps: Wrapper em Document com extends
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
