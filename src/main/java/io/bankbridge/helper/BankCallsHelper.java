package io.bankbridge.helper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * This helper class contains utility methods used by handler class.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class BankCallsHelper {

    /**
     * This method generates a JSON from a Java object and returns the generated
     * JSON as a string.
     *
     * @param result: List of Map<String, String>
     * @return resultAsString : String
     */
    public static String convertJavaObjectToJson(List<Map<String, String>> result) {
        try {
            String resultAsString = new ObjectMapper().writeValueAsString(result);
            return resultAsString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while processing request.");
        }
    }
}