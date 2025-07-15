package com.hub4.model;

import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.BaseFont;

public class Section {
    private final Alignment alignment;
    private final String content;
    private final BaseFont baseFont;
    private final int fontSize;
    private final SectionType sectionType;

    public Section(SectionType sType, String content, BaseFont baseFont, int fontSize) {
        this.sectionType = sType;

        if(sType == SectionType.TITLE || sType == SectionType.ANNEX) {
            this.alignment = Alignment.Center;
        } else {
            this.alignment = Alignment.Justify;
        }

        this.content = content;
        this.baseFont = baseFont;
        this.fontSize = fontSize;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getContent() {
        return content;
    }

    public BaseFont getBaseFont() {
        return baseFont;
    }

    public int getFontSize() {
        return fontSize;
    }

    public SectionType getSectionType() {
        return sectionType;
    }
}
