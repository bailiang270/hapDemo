package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by 12996 on 2018/6/5.
 */
public class TokenCache {

    //常量
    public static final String TOKEN_PREFIX = "token_";

    //1.声明日志
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //2.声明静态内存块，guava缓存
    //initialCapacity(1000)设置初始化容量为1000。
    //maximumSize,缓存超过最大缓存量，guava使用LRU算法移除缓存。
    //expireAfterAccess，设置缓存有效期（12小时）。
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    //3.外部调用方法
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }

}
