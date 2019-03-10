package io.bankbridge.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.bankbridge.constant.CommonConstants;
import io.bankbridge.helper.BankCallsHelper;
import io.bankbridge.model.BankModel;
import io.bankbridge.model.BankModelList;
import spark.Request;
import spark.Response;

/**
 *
 * This class is used to initialize cache with bank details and handles the
 * request for retrieving bank details from cache.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class BanksCacheBased {

    // Removed hard-coded values from the entire class.
    public static CacheManager cacheManager;

    public static void setCacheManager(CacheManager cacheManager) {
        BanksCacheBased.cacheManager = cacheManager;
    }

    /**
     * This method is used to instantiate and initialize cache with values stored in
     * "banks-v1.json" file.
     */
    public static void init() throws Exception {
        cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().withCache(CommonConstants.BANKS, CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String.class, BankModel.class, ResourcePoolsBuilder.heap(10)))
                .build();

        try {
            cacheManager.init();
            // Changed the raw type cache to generic type.
            Cache<String, BankModel> cache = cacheManager.getCache(CommonConstants.BANKS, String.class,
                    BankModel.class);
            BankModelList bankModelList = new ObjectMapper().readValue(
                    Thread.currentThread().getContextClassLoader().getResource(CommonConstants.BANK_V1_JSON),
                    BankModelList.class);

            // 1. Changed the enhanced for loop with forEach loop.
            // 2. Changed the cache put statement to store bankModel instead of name as a
            // value in cache.
            bankModelList.getBanks().forEach(bankModel -> cache.put(bankModel.getBic(), bankModel));

        } catch (Exception e) {
            throw new RuntimeException("Error initializing cache", e);
        }
    }

    /**
     * This method handles the get request to retrieve bank details from cache.
     *
     * @param request: Request
     * @param response: Response
     * @return resultAsJsonString : String
     */
    public static String handle(Request request, Response response) {

        // Changed the list of raw type Map to generic type Map.
        List<Map<String, String>> result = new ArrayList<>();
        cacheManager.getCache(CommonConstants.BANKS, String.class, BankModel.class).forEach(entry -> {
            Map<String, String> map = new HashMap<>();
            map.put(CommonConstants.NAME, entry.getValue().getName());
            map.put(CommonConstants.ID, entry.getKey());
            result.add(map);
        });

        // extracted the code of "Java object to Json string" conversion into a another
        // method in helper class.
        return BankCallsHelper.convertJavaObjectToJson(result);

    }

}
