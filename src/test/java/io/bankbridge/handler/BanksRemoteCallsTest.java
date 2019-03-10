package io.bankbridge.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.bankbridge.model.BankModel;
import io.bankbridge.util.HttpClient;
import spark.Request;
import spark.Response;

class BanksRemoteCallsTest {

    @Mock
    private static HttpClient httpClient;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        Map<String, String> config = new HashMap<>();
        config.put("Credit Sweets", "http://localhost:1234/cs");
        BanksRemoteCalls.setConfig(config);
    }

    @Test
    void testHandle() throws Exception {
        assertNotNull(httpClient);
        BankModel csBank = new BankModel();
        csBank.setAuth("OpenID");
        csBank.setBic("5678");
        csBank.setCountryCode("CH");

        when(httpClient.executeGetRequest("http://localhost:1234/cs", BankModel.class)).thenReturn(csBank);

        BanksRemoteCalls.setHttpClient(httpClient);

        Request request = null;
        Response response = null;
        String actual = BanksRemoteCalls.handle(request, response);
        String expected = "[{\"name\":\"Credit Sweets\",\"id\":\"5678\"}]";
        assertNotNull(actual, "Handle method should not return null string.");
        assertEquals(expected, actual,
                "Result string from handle method should return the correct Bank id and Bank name");

    }

}