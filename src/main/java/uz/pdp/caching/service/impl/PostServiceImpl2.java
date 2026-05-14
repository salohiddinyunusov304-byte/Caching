package uz.pdp.caching.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uz.pdp.caching.entity.Post;
import uz.pdp.caching.payload.PostUpdater;
import uz.pdp.caching.repository.PostRepository;
import uz.pdp.caching.service.PostService;
import uz.pdp.caching.service.RedisService;

import java.time.Duration;
import java.util.concurrent.TimeUnit;



@Service
@RequiredArgsConstructor
public class PostServiceImpl2 implements PostService {
    private final PostRepository postRepository;
    private final RedisService redisService;

    public static final String POST_GET_KEY = "POST_GET_KEY:";
    public static final Integer POST_GET_KEY_TTL = 2;

//    @Override
//    @SneakyThrows
//    @Cacheable(cacheNames = "posts", key = "#id")
//    public Post getPostById(Integer id) {
//        Post post = postRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found..."));
//
//        TimeUnit.SECONDS.sleep(2);
//        return post;
//    }
///  caching with redis
    @Override
    @SneakyThrows
    public Post getPostById(Integer id) {
        Post postFromRedis = redisService.get(POST_GET_KEY + id, Post.class);
        if (postFromRedis != null) {
            return postFromRedis;
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found..."));
        TimeUnit.SECONDS.sleep(2);

        redisService.put(POST_GET_KEY + id, post, Duration.ofHours(POST_GET_KEY_TTL));
        return post;
    }

//    @Override
//    @CacheEvict(cacheNames = "posts", key = "#id")
//    public void deleteById(Integer id) {
//        postRepository.deleteById(id);
//    }
///  caching with redis
    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
        redisService.delete(POST_GET_KEY + id);
    }

//    @Override
//    @CachePut(cacheNames = "posts", key = "#updater.id()")
//    public void updateById(PostUpdater updater) {
//        Post post = (Post) get(updater.id());
//
//        post.setUserId(updater.userId());
//        post.setTitle(updater.title());
//        post.setBody(updater.body());
//        Post save = postRepository.save(post);
//    }

    /// caching with redis
    @Override
    public void updateById(PostUpdater updater) {

        Post post = getPostById(updater.id());

        post.setUserId(updater.userId());
        post.setTitle(updater.title());
        post.setBody(updater.body());

        Post updated = postRepository.save(post);

        redisService.put(
                POST_GET_KEY + updated.getId(),
                updated,
                Duration.ofHours(POST_GET_KEY_TTL)
        );
    }
}
