package io.bankbridge.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.constant.CommonConstants;
import io.bankbridge.helper.BankCallsHelper;
import io.bankbridge.model.BankModel;
import io.bankbridge.util.HttpClient;
import spark.Request;
import spark.Response;

/**
 *
 * This class is used to initialize config for retrieving bank details from
 * remote server.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class BanksRemoteCalls {

    // Changed the raw type Map to generic type Map.
    private static Map<String, String> config;
    private static HttpClient httpClient;

    public static void setConfig(Map<String, String> config) {
        BanksRemoteCalls.config = config;
    }

    public static void setHttpClient(HttpClient httpClient) {
        BanksRemoteCalls.httpClient = httpClient;
    }

    /**
     * This method is used to initialize Map object with values stored in
     * "banks-v2.json" file. Also, it initialize httpClient.
     */
    public static void init() throws Exception {

        // Changed the 2nd argument of readValue method from Map.class to TypeReference
        // object.
        config = new ObjectMapper().readValue(
                Thread.currentThread().getContextClassLoader().getResource(CommonConstants.BANK_V2_JSON),
                new TypeReference<Map<String, String>>() {
                });

        httpClient = new HttpClient();
    }

    /**
     * This method handles the get request to retrieve bank details from remote
     * server.
     *
     * @param request: Request
     * @param response: Response
     * @return resultAsJsonString : String
     */
    // Implemented the remote call handle method.
    public static String handle(Request request, Response response) {

        List<Map<String, String>> result = new ArrayList<>();
        config.entrySet().forEach(entry -> {
            Map<String, String> map = new HashMap<>();
            map.put(CommonConstants.NAME, entry.getKey());

            try {
                BankModel bankInfo = httpClient.executeGetRequest(entry.getValue(), BankModel.class);
                map.put(CommonConstants.ID, bankInfo.getBic());
                result.add(map);
            } catch (Exception e) {
                throw new RuntimeException("Error loading remote bank data.");
            }

        });
        return BankCallsHelper.convertJavaObjectToJson(result);

    }

}
