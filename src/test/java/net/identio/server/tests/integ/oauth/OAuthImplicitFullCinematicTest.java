package net.identio.server.tests.integ.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import net.identio.server.boot.IdentioServerApplication;
import net.identio.server.model.ProtocolType;
import net.identio.server.model.State;
import net.identio.server.model.api.AuthMethodResponse;
import net.identio.server.model.api.AuthSubmitRequest;
import net.identio.server.model.api.AuthSubmitResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = IdentioServerApplication.class)
@TestPropertySource(properties = {"identio.config: src/test/resources/oauth-server-config/identio-config.yml",
        "identio.public.fqdn: https://localhost",
        "logging.config: src/test/resources/oauth-server-config/logback.xml"})
public class OAuthImplicitFullCinematicTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    private static final String AUTHENTICATION_URL = "/#!/auth/";

    @Test
    public void oAuthAuthorizeValidRequest() {

        ResponseEntity<String> initialRequestResponse = restTemplate.exchange(getUrlWithPort("/oauth/authorize?client_id=test&redirect_uri=http://example.com/cb&response_type=token&scope=scope.test.1 scope.test.2&state=1234"),
                HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                String.class);

        // Redirect to the login page
        String redirectUrl = initialRequestResponse.getHeaders().getFirst(HttpHeaders.LOCATION);

        assertEquals(HttpStatus.FOUND, initialRequestResponse.getStatusCode());
        assertTrue(redirectUrl.startsWith(getUrlWithPort(AUTHENTICATION_URL)));


        // Extract session cookie and transactionId
        String sessionIdCookie = getSessionCookie(initialRequestResponse);
        String transactionId = getTransactionId(redirectUrl);

        assertTrue(sessionIdCookie.startsWith("identioSession="));
        assertNotNull(transactionId);

        // Request authentication methods
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionIdCookie);
        headers.add("X-Transaction-ID", transactionId);

        ResponseEntity<AuthMethodResponse[]> authMethodResponse = restTemplate.exchange(
                getUrlWithPort("/api/auth/methods"),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                AuthMethodResponse[].class);

        assertEquals(HttpStatus.OK, authMethodResponse.getStatusCode());
        assertEquals(authMethodResponse.getBody().length, 1);
        assertEquals(authMethodResponse.getBody()[0].getName(), "Local");
        assertEquals(authMethodResponse.getBody()[0].getType(), "local");

        // Authenticate with local method
        AuthSubmitRequest authenticationSubmit = new AuthSubmitRequest().setLogin("johndoe").setPassword("password")
                .setMethod("Local");

        ResponseEntity<AuthSubmitResponse> authSubmitResponseEntity = restTemplate.exchange(
                getUrlWithPort("/api/auth/submit/password"),
                HttpMethod.POST,
                new HttpEntity<>(authenticationSubmit, headers),
                AuthSubmitResponse.class);

        // Check that the authentication is successful and that we get a response
        AuthSubmitResponse authSubmitResponse = authSubmitResponseEntity.getBody();

        assertEquals(HttpStatus.OK, authSubmitResponseEntity.getStatusCode());
        assertNotNull(authSubmitResponse);

        assertEquals(authSubmitResponse.getState(), State.RESPONSE);
        assertEquals(authSubmitResponse.getProtocolType(), ProtocolType.OAUTH);
        assertEquals(authSubmitResponse.getRelayState(), "1234");
        assertTrue(authSubmitResponse.getResponse().matches("^http://example.com/cb#expires_in=2400&token_type=Bearer&access_token=.*&state=1234"));

        // Parse and validate JWT
        Pattern pattern = Pattern.compile("^http://example.com/cb#expires_in=2400&token_type=Bearer&access_token=(.*)&state=1234");
        Matcher matcher = pattern.matcher(authSubmitResponse.getResponse());

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.RSA256(getPublicSigningKey());
        } catch (Exception e) {
            fail();
        }

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("https://localhost")
                .withSubject("johndoe")
                .withAudience("Test Client")
                .withClaim("scope", "scope.test.1 scope.test.2")
                .build();

        if (matcher.find()) {
            verifier.verify(matcher.group(1));
        }
        else {
            fail();
        }

    }

    private RSAKey getPublicSigningKey() throws Exception {

        FileInputStream fis = new FileInputStream(
                "src/test/resources/oauth-server-config/default-sign-certificate.p12");

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(fis, "password".toCharArray());

        Enumeration<String> aliases = ks.aliases();

        String alias = aliases.nextElement();

        return (RSAKey) (ks.getCertificate(alias)).getPublicKey();

    }


    private String getUrlWithPort(String url) {

        return "http://localhost:" + port + url;
    }

    private String getSessionCookie(ResponseEntity<?> response) {

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private String getTransactionId(String url) {

        return url.substring(getUrlWithPort(AUTHENTICATION_URL).length());
    }
}
