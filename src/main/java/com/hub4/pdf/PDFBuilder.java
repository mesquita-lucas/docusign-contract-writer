package com.hub4.pdf;

import com.hub4.api.dto.ContractDTO;
import com.hub4.domain.json.ContentLoader;
import com.hub4.domain.model.Contract;
import com.hub4.domain.model.ContractContents;

import java.io.IOException;

public class PDFBuilder {
    private final ContractDTO contractDTO;
    private final Contract contract;

    public PDFBuilder(ContractDTO contractDTO) {
        this.contractDTO = contractDTO;

        ContentLoader loader = new ContentLoader(contractDTO);
        ContractContents contractContents = loader.getContents();

        this.contract = new Contract(contractContents);
    }

    public byte[] buildContract() throws IOException {
        contract.writeContract();
        contract.addHeader();
        //contract.addAnnexImages(contractDTO.images());

        return contract.saveContract();
    }

}
