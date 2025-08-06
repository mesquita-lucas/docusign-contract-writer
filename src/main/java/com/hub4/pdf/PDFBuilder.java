package com.hub4.pdf;

import com.hub4.api.dto.ContractDTO;
import com.hub4.domain.json.ContentLoader;
import com.hub4.domain.utils.AnnexImageDrawer;
import com.hub4.domain.model.ContractContents;
import com.hub4.domain.utils.ContractWriter;
import com.hub4.domain.utils.LogoStamper;
import com.hub4.domain.utils.PDFMerger;
import java.io.IOException;


public class PDFBuilder {
    private final ContractDTO contractDTO;
    private final ContractContents contractContents;
    private byte[] pdf;

    public PDFBuilder(ContractDTO contractDTO) {
        this.contractDTO = contractDTO;

        ContentLoader loader = new ContentLoader(contractDTO);
        this.contractContents = loader.getContents();
    }

    public PDFBuilder write() throws IOException {
        ContractWriter writer = new ContractWriter(contractContents);
        writer.writeTextToPDF();

        this.pdf = writer.save();

        return this;
    }

    public PDFBuilder addAnnexImages() throws IOException {
        AnnexImageDrawer drawer = new AnnexImageDrawer(contractDTO.images());
        drawer.draw();
        byte[] annexDocumentInBytes = drawer.save();

        PDFMerger merger = new PDFMerger(pdf,  annexDocumentInBytes);
        merger.merge();

        this.pdf = merger.saveFullContract();

        return this;
    }

    public PDFBuilder stampLogo() throws IOException {
        LogoStamper stamper = new LogoStamper(pdf);
        stamper.stamp();

        this.pdf = stamper.save();

        return this;
    }

    public byte[] build(){
        return pdf;
    }
}
