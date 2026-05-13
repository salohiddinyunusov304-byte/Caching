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

import java.util.concurrent.TimeUnit;

import static javax.swing.UIManager.get;


@Service
@RequiredArgsConstructor
public class PostServiceImpl2 implements PostService {
    private final PostRepository postRepository;

    @Override
    @SneakyThrows
    @Cacheable(cacheNames = "posts", key = "#id")
    public Post getPostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found..."));

        TimeUnit.SECONDS.sleep(2);
        return post;
    }

    @Override
    @CacheEvict(cacheNames = "posts", key = "#id")
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    @CachePut(cacheNames = "posts", key = "#updater.id()")
    public void updateById(PostUpdater updater) {
        Post post = (Post) get(updater.id());

        post.setUserId(updater.userId());
        post.setTitle(updater.title());
        post.setBody(updater.body());
        Post save = postRepository.save(post);
    }
}
