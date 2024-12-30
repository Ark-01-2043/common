package com.dnpa.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

public class CachingUtil {
    @Autowired
    CacheManager cacheManager;

    public void clearRedisCacheByName(String cachename) {
        cacheManager.getCache(cachename).clear();
    }
}
