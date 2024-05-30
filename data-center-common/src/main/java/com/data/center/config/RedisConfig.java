package com.data.center.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //value序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //hash的key序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //hash的value序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }

//    @Bean
//    public RedissonClient redissonClient(RedisTemplate template) {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://" +  + ":" + properties.getPort())
//                .setPassword(properties.getPassword())
//                .setConnectionPoolSize(properties.getPoolSize())
//                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
//                .setIdleConnectionTimeout(properties.getIdleTimeout())
//                .setConnectTimeout(properties.getConnectTimeout())
//                .setKeepAlive(properties.isKeepAlive())
//                .setPingConnectionInterval(properties.getPingInterval())
//                .setRetryAttempts(properties.getRetryAttempts())
//                .setRetryInterval(properties.getRetryInterval())
//        ;
//        config.setCodec(JsonJacksonCodec.INSTANCE);
//        return Redisson.create(config);
//    }
}
