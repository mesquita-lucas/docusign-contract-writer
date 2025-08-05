package com.hub4.docusign;

import com.docusign.esign.client.ApiClient;

public record AuthData(
    ApiClient apiClient,
    String accessToken,
    String accountId
) {}