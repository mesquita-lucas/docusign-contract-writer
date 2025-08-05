package com.hub4.pdf;

import com.hub4.api.dto.ContractDTO;
import com.hub4.domain.json.ContentLoader;
import com.hub4.domain.utils.AnnexImageDrawer;
import com.hub4.domain.model.ContractContents;
import com.hub4.domain.utils.ContractWriter;
import com.hub4.domain.utils.LogoStamper;

import java.io.FileOutputStream;
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

    public byte[] build() throws IOException {
        return pdf;
    }

    public byte[] buildForDebug() throws IOException {
        System.out.println("--- INICIANDO PROCESSO DE BUILD PARA DEBUG ---");

        this.write();
        saveForDebug("1_texto_escrito.pdf", this.pdf);

        this.addAnnexImages();
        saveForDebug("2_com_anexo.pdf", this.pdf);

        this.stampLogo();
        saveForDebug("3_final_com_logo.pdf", this.pdf);

        System.out.println("--- PROCESSO DE BUILD DE DEBUG FINALIZADO ---");
        return this.pdf;
    }

    private void saveForDebug(String nomeArquivo, byte[] dados) throws IOException {
        if (dados == null) {
            System.out.println("AVISO: Dados para salvar '" + nomeArquivo + "' estão nulos.");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(nomeArquivo)) {
            fos.write(dados);
        }
        System.out.println("Salvo arquivo de debug: " + nomeArquivo);
    }
}
