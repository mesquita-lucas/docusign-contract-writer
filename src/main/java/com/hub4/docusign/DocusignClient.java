package com.hub4.docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.hub4.api.dto.ContractDTO;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;

public class DocusignClient {
    private final ConfigLoader config;
    private final ContractDTO contractDTO;
    private final String pdf;

    public DocusignClient(ContractDTO dto, byte[] contract) throws IOException {
        this.config = new ConfigLoader("/app/secrets/app.config");
        this.contractDTO = dto;
        this.pdf = Base64.getEncoder().encodeToString(contract);
    }

    public void sendEnvelope(){
        final String DevCenterPage = "https://developers.docusign.com/platform/auth/consent";

        try
        {
            DocusignAuthenticator authenticator = DocusignAuthenticator.forProdEnvironment(config);
            AuthData auth = authenticator.authenticate(config.get("apiAccountId"));

            EnvelopeDefinition envelope = EnvelopeBuilder.build(contractDTO, pdf);

            EnvelopesApi envelopesApi = new EnvelopesApi(auth.apiClient());
            EnvelopeSummary result = envelopesApi.createEnvelope(auth.accountId(), envelope);

            System.out.println("Successfully created envelope with envelope ID:" + result.getEnvelopeId());
        } catch (ApiException e) {

            if(e.getMessage().contains("consent_required")) {
                try {
                    String consentUrl = "https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature"
                            + "&client_id=" + config.get("clientId")
                            + "&redirect_uri=" + DevCenterPage;

                    Desktop.getDesktop().browse(URI.create(consentUrl));
                } catch (Exception ex) {
                    System.out.println("Erro ao abrir o navegador: " + e.getMessage());
                }
            } else {
                System.out.println("Erro de API: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Erro ao enviar o envelope: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
