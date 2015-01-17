package org.netcs;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * SPRING WWW Application Redis Connection Configuration
 *
 * @author ichatz@gmail.com
 */
@Configuration
public class RedisConfiguration {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = Logger.getLogger(RedisConfiguration.class);

    @Bean
    JedisConnectionFactory jedisConnFactory() {
        final JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("127.0.0.1");
        factory.setPort(6379);
        factory.setDatabase(1);
        factory.setUsePool(true);
        return factory;
    }

    @Bean
    RedisTemplate redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(jedisConnFactory());
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
