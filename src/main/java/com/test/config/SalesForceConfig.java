package com.test.config;

import com.force.api.ApiConfig;
import com.force.api.ApiSession;
import com.force.api.ForceApi;
import com.sforce.soap.partner.CallOptions_element;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "salesforce")
public class SalesForceConfig {
    private String version;
    private Production production;
    private Sandbox sandbox;

    @Getter
    @Setter
    public static class Production {
        private String tokenEndpoint;
        private String clientId;
        private String clientSecret;
        private String redirectUri;

        @Override
        public String toString() {
            return "Production{" + "tokenEndpoint=" + tokenEndpoint + ", clientId=" + (clientId != null ? "<sensitive>" : null) + ", clientSecret=" + (clientSecret != null ? "<sensitive>" : null) + ", redirectUri=" + redirectUri + '}';
        }
    }

    @Getter
    @Setter
    public static class Sandbox {
        private String tokenEndpoint;
        private String clientId;
        private String clientSecret;
        private String redirectUri;

        @Override
        public String toString() {
            return "Sandbox{" + "tokenEndpoint=" + tokenEndpoint + ", clientId=" + (clientId != null ? "<sensitive>" : null) + ", clientSecret=" + (clientSecret != null ? "<sensitive>" : null) + ", redirectUri=" + redirectUri + '}';
        }
    }

    public static ForceApi forceApiClient(String sessionID, String restApiUrl) {
        var apiVersion = new SalesForceConfig().getVersion();
        if (apiVersion == null) apiVersion = "50.0";
        ApiConfig apiConfigObject = new ApiConfig().setApiVersionString("v" + apiVersion);
        ApiSession sessionObject = new ApiSession().setAccessToken(sessionID).setApiEndpoint(restApiUrl);
        return new ForceApi(apiConfigObject, sessionObject);


    }

    public static PartnerConnection buildSalesforcePartnerConnection(final String userSessionId, final String serverUrl, final boolean isSandbox) throws ConnectionException {
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(userSessionId);
        config.setServiceEndpoint(serverUrl);
        if (isSandbox)
            config.setRequestHeader("ClientID", "HICGlobalSolutions/ConnectedSandbox/");
        else
            config.setRequestHeader("ClientID", "HICGlobalSolutions/Connectedorg/");
        PartnerConnection pc = new PartnerConnection(config);
        CallOptions_element elem = new CallOptions_element();
        if (isSandbox)
            elem.setClient("HICGlobalSolutions/ConnectedSandbox/");
        else
            elem.setClient("HICGlobalSolutions/Connectedorg/");
        pc.__setCallOptions(elem);
        return pc;
    }

    public static PartnerConnection buildSalesforcePartnerConnection(final String userSessionId, final String serverUrl) throws ConnectionException {
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(userSessionId);
        config.setServiceEndpoint(serverUrl);
        return new PartnerConnection(config);
    }
}