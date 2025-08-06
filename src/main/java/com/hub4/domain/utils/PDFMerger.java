package com.hub4.domain.utils;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFMerger {
    private final PDDocument pdf;
    private final PDDocument annexPdf;

    public PDFMerger(byte[] pdfBytes, byte[] annexPdfBytes) throws IOException {
        this.pdf = PDDocument.load(pdfBytes);
        this.annexPdf = PDDocument.load(annexPdfBytes);
    }

    public void merge(){
        annexPdf.getPages().forEach(page -> {
            try {
                pdf.importPage(page);
            } catch (IOException e) {
                System.out.println("The system was unable to merge documents");
                throw new RuntimeException(e.getMessage());
            }
        });
    }

    public byte[] saveFullContract() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pdf.save(baos);

        pdf.close();
        annexPdf.close();
        return baos.toByteArray();
    }
}
