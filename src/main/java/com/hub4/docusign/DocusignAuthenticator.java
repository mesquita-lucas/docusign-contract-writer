package com.hub4.docusign;

import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.client.auth.OAuth.OAuthToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DocusignAuthenticator {
    private final ConfigLoader config;
    private final ApiClient apiClient;

    static final String DEVELOPMENT_ENVIRONMENT_ESIG_URL = "https://demo.docusign.net/restapi";
    static final String DEVELOPMENT_ENVIRONMENT_AUTH_URL = "account-d.docusign.com";

    static final String PRODUCTION_ENVIRONMENT_ESIG_URL = "https://na2.docusign.net/restapi";
    static final String PRODUCTION_ENVIRONMENT_AUTH_URL = "account.docusign.com";

    private DocusignAuthenticator(ConfigLoader config, String eSignatureURL, String authURL) {
        this.config = config;

        this.apiClient = new ApiClient(eSignatureURL);
        apiClient.setOAuthBasePath(authURL);
    }

    static DocusignAuthenticator forDevEnvironment(ConfigLoader config) {
        return new DocusignAuthenticator(config, DEVELOPMENT_ENVIRONMENT_ESIG_URL, DEVELOPMENT_ENVIRONMENT_AUTH_URL);
    }

    static DocusignAuthenticator forProdEnvironment(ConfigLoader config) {
        return new DocusignAuthenticator(config, PRODUCTION_ENVIRONMENT_ESIG_URL, PRODUCTION_ENVIRONMENT_AUTH_URL);
    }

    public AuthData authenticate() throws IOException, ApiException {
        InputStream keyInputStream = new FileInputStream("/etc/secrets/private.key");

        byte[] privateKey = keyInputStream.readAllBytes();

        OAuthToken token = apiClient.requestJWTUserToken(
                config.get("clientId"),
                config.get("userId"),
                List.of("signature", "impersonation"),
                privateKey,
                3600
        );

        String accessToken = token.getAccessToken();
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);

        UserInfo userInfo = apiClient.getUserInfo(accessToken);
        String accountId = userInfo.getAccounts().getFirst().getAccountId();

        keyInputStream.close();

        return new AuthData(apiClient, accessToken, accountId);
    }
}
