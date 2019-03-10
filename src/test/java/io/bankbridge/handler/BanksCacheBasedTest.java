package io.bankbridge.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.bankbridge.constant.CommonConstants;
import io.bankbridge.model.BankModel;
import spark.Request;
import spark.Response;

class BanksCacheBasedTest {

    public static CacheManager cacheManager;

    @BeforeAll
    public static void setup() {
        cacheManager = CacheManagerBuilder
                .newCacheManagerBuilder().withCache(CommonConstants.BANKS, CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(String.class, BankModel.class, ResourcePoolsBuilder.heap(10)))
                .build();

        cacheManager.init();

        Cache<String, BankModel> cache = cacheManager.getCache(CommonConstants.BANKS, String.class, BankModel.class);
        BanksCacheBased.setCacheManager(cacheManager);

        BankModel bankModel1 = new BankModel();
        bankModel1.setAuth("SSL");
        bankModel1.setBic("9870");
        bankModel1.setCountryCode("PT");
        bankModel1.setName("Banco de espiritu santo");
        BankModel bankModel2 = new BankModel();
        bankModel2.setAuth("OpenID");
        bankModel2.setBic("5678");
        bankModel2.setCountryCode("CH");
        bankModel2.setName("Credit Sweets");
        cache.put("9870", bankModel1);
        cache.put("5678", bankModel2);
    }

    @Test
    void testHandle() {
        assertNotNull(cacheManager);
        Request request = null;
        Response response = null;
        String actual = BanksCacheBased.handle(request, response);
        String expected = "[{\"name\":\"Banco de espiritu santo\",\"id\":\"9870\"},{\"name\":\"Credit Sweets\",\"id\":\"5678\"}]";
        assertNotNull(actual, "Handle method should not return null string.");
        assertEquals(expected, actual,
                "Result string from handle method should return the correct Bank id and Bank name");
    }

    @AfterAll
    public static void teardown() {
        cacheManager.close();
    }

}