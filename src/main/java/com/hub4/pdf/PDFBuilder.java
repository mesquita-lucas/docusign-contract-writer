package com.hub4.pdf;

import com.hub4.api.dto.ContractDTO;
import com.hub4.domain.json.ContentLoader;
import com.hub4.domain.utils.AnnexImageDrawer;
import com.hub4.domain.model.ContractContents;
import com.hub4.domain.utils.ContractWriter;
import com.hub4.domain.utils.LogoStamper;

import java.io.IOException;

public class PDFBuilder {
    private final ContractDTO contractDTO;
    private final ContractContents contractContents;
    private byte[] pdf;

    public PDFBuilder(ContractDTO contractDTO) {
        this.contractDTO = contractDTO;
        ContentLoader loader;

        if ("SP".equals(contractDTO.location())){
            loader = ContentLoader.loadTemplateForSP(contractDTO);
        } else if ("BSB".equals(contractDTO.location())) {
            loader = ContentLoader.loadTemplateForBSB(contractDTO);
        } else {
            throw new IllegalArgumentException("Invalid location: " + contractDTO.location());
        }

        this.contractContents = loader.getContents();
    }

    public PDFBuilder write() throws IOException {
        int numberOfImages = contractDTO.images().size();
        ContractWriter writer = new ContractWriter(contractContents, numberOfImages > 3);
        writer.writeTextToPDF();

        this.pdf = writer.save();

        return this;
    }

    public PDFBuilder addAnnexImages() throws IOException {
        AnnexImageDrawer drawer = new AnnexImageDrawer(pdf, contractDTO.images());
        drawer.draw();

        this.pdf = drawer.save();

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
