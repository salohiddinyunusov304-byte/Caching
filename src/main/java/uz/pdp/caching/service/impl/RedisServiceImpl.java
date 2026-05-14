package uz.pdp.caching.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import uz.pdp.caching.service.RedisService;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {

        Object o = redisTemplate.opsForValue().get(key);

        if (o == null) {
            return null;
        }

        return objectMapper.convertValue(o, clazz);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}