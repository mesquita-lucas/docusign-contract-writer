package com.hub4.domain.utils;

import com.hub4.domain.model.ContractContents;
import com.hub4.domain.model.Section;
import com.hub4.domain.model.SectionType;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ContractWriter {
    private final ContractContents contents;
    private final boolean documentNeedsExtraAnnexPage;
    private final Document document;

    public ContractWriter(ContractContents contents, boolean documentNeedsExtraAnnexPage) {
        this.contents = contents;
        this.documentNeedsExtraAnnexPage = documentNeedsExtraAnnexPage;
        this.document = new Document(70, 70, 100, 50);
    }

    public void writeTextToPDF() throws IOException {
        List<Section> sectionList = loadSectionsList(contents);

        sectionList.forEach(section -> {
            if(section.getSectionType().equals(SectionType.ANNEX)) document.add(ControlElement.NEWPAGE);

            try {
                document.add(paragraphFor(section));
                document.add(blankLine());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao adicionar parágrafo para a seção: " + section.getSectionType(), e);
            }
        });

        if (documentNeedsExtraAnnexPage) {
            Section annexSection = sectionList.stream()
                    .filter(s -> SectionType.ANNEX.equals(s.getSectionType()))
                    .findFirst()
                    .orElse(null);

            if (annexSection != null) {
                document.add(ControlElement.NEWPAGE);
                document.add(paragraphFor(annexSection));
            }
        }
    }

    public byte[] save() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            document.save(baos);

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

    private Paragraph paragraphFor(Section section) throws IOException {
        Paragraph paragraph = new Paragraph();

        if(section.getSectionType().equals(SectionType.SIGNATURE)){
            paragraph.addText(section.getContent(), section.getFontSize(), PDType1Font.HELVETICA);
        } else {
            paragraph.addMarkup(section.getContent(), section.getFontSize(), section.getBaseFont());
        }

        paragraph.setAlignment(section.getAlignment());
        paragraph.setLineSpacing(1.8f);

        return paragraph;
    }

    private Paragraph blankLine() throws IOException {
        Paragraph par = new Paragraph();
        par.addText(" ", 11, PDType1Font.HELVETICA);

        return par;
    }
}
