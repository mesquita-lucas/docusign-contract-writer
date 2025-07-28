package com.hub4.docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.hub4.dao.MockContractDAO;
import com.hub4.dto.ContractDTO;
import com.hub4.pdf.ContractBuilder;

import java.io.*;
import java.awt.Desktop;
import java.net.URI;


public class App {
    static String DevCenterPage = "https://developers.docusign.com/platform/auth/consent";

    public static void main(String[] args) throws IOException {
        System.out.println(">> Entrou no App.main()");
        try
        {
            ConfigLoader config = new ConfigLoader("secrets/app.config");

            ContractDTO contractDTO = MockContractDAO.get();

            DocusignAuthenticator authenticator = DocusignAuthenticator.forDevEnvironment(config);
            AuthData auth = authenticator.authenticate();

            byte[] pdf = ContractBuilder.build();

            EnvelopeDefinition envelope = EnvelopeBuilder.build(contractDTO, pdf);

            EnvelopesApi envelopesApi = new EnvelopesApi(auth.apiClient());
            EnvelopeSummary result = envelopesApi.createEnvelope(auth.accountId(), envelope);

            System.out.println("Successfully created envelope with envelope ID:" + result.getEnvelopeId());
        } catch (ApiException e) {

            if(e.getMessage().contains("consent_required")) {
                try
                {
                    String consentUrl = "https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature"
                            + "&client_id=" + new ConfigLoader("secrets/app.config").get("clientId")
                            + "&redirect_uri=" + DevCenterPage;

                    Desktop.getDesktop().browse(URI.create(consentUrl));
                } catch (Exception ex) {
                    System.out.println("Erro ao abrir o navegador: " + e.getMessage());
                }
            } else {
                System.out.println("Erro de API: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
