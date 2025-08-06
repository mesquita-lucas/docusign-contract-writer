package com.hub4.service;

import com.hub4.docusign.DocusignClient;
import com.hub4.api.dto.ContractDTO;
import com.hub4.pdf.PDFBuilder;

import java.io.IOException;

public class ContractHandler {
    private final ContractDTO contractDTO;

    public ContractHandler(ContractDTO contractDTO) {
        this.contractDTO = contractDTO;
    }

    public void handleContract() throws IOException {
        PDFBuilder builder = new PDFBuilder(contractDTO);

        byte[] contractInBytes = builder.write()
                .addAnnexImages()
                .stampLogo()
                .build();

        DocusignClient client = new DocusignClient(contractDTO, contractInBytes);
        client.sendEnvelope();
    }
}