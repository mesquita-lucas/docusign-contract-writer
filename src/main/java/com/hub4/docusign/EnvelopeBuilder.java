package com.hub4.docusign;

import com.docusign.esign.model.*;
import com.hub4.api.dto.ContractDTO;
import rst.pdfbox.layout.text.Position;

import java.io.IOException;
import java.util.List;

public class EnvelopeBuilder {
    private static final Position consignorSigningPosition = new Position(74, 451); //first to show
    private static final Position consigneeSigningPosition = new Position(74, 591); //second to show

    public static EnvelopeDefinition build(ContractDTO contractDTO, String encodedPDF) throws IOException {
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Contrato de consignação Hub4 para " + contractDTO.prodBrand() + " " + contractDTO.prodModel());
        envelope.setStatus("sent");

        Tabs consignorTabs = createTabs(consignorSigningPosition);
        Tabs consigneeTabs = createTabs(consigneeSigningPosition);

        Signer consignor = createSigner(
                contractDTO.consignorName(),
                contractDTO.consignorEmail(),
                consignorTabs,
                "1"
        );

        ConfigLoader consigneeData = new ConfigLoader("/etc/secrets/consigneedata.config");

        Signer consignee = createSigner(
                consigneeData.get("name"),
                consigneeData.get("email"),
                consigneeTabs,
                "2"
        );

        Recipients recipients = new Recipients();
        recipients.setSigners(List.of(consignor, consignee));
        envelope.setRecipients(recipients);

        Document document = new Document();

        document.setDocumentBase64(encodedPDF);
        document.setName("contrato-de-consignacao.pdf");
        document.setFileExtension("pdf");
        document.setDocumentId("1");
        envelope.setDocuments(List.of(document));

        return envelope;
    }

    private static Signer createSigner(String name, String email, Tabs tabs, String routingOrder) {
        Signer signer = new Signer();

        signer.setName(name);
        signer.setEmail(email);
        signer.setRecipientId(routingOrder);
        signer.setTabs(tabs);
        signer.setRoutingOrder(routingOrder);

        return signer;
    }

    private static Tabs createTabs(Position tabPosition) {
        SignHere signingTab = createSignHere(tabPosition);

        Tabs tabs = new Tabs();
        tabs.setSignHereTabs(List.of(signingTab));

        return tabs;
    }

    private static SignHere createSignHere(Position signHerePosition){
        SignHere signHere = new SignHere();

        signHere.setDocumentId("1");
        signHere.setPageNumber("3");
        signHere.setXPosition(String.valueOf((int) signHerePosition.getX()));
        signHere.setYPosition(String.valueOf((int) signHerePosition.getY()));

        return signHere;
    }
}
