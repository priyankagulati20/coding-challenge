package io.bankbridge.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * Basic HTTP client class, implementing only GET request execution.
 * Implementation based on Apache HttpComponents library.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class HttpClient {

    private CloseableHttpClient httpClient;

    public HttpClient() {
        httpClient = HttpClientBuilder.create().build();
    }

    public void shutdown() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T executeGetRequest(String url, Class<T> responseClass) throws Exception {
        if (httpClient == null) {
            throw new IllegalStateException("HTTP client may have not been initialized correctly");
        }
        HttpGet request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);

        // verify the valid error code first
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            throw new RuntimeException("Error while executing the request, HTTP error code : " + statusCode);
        }

        // return the the response object converted into java object.
        return new ObjectMapper().readValue(EntityUtils.toString(response.getEntity()), responseClass);

    }

}