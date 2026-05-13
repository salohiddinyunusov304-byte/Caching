package uz.pdp.caching.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.pdp.caching.entity.Post;
import uz.pdp.caching.payload.PostUpdater;
import uz.pdp.caching.repository.PostRepository;
import uz.pdp.caching.service.PostService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static javax.swing.UIManager.get;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ConcurrentHashMap<Integer, Post> postCache = new ConcurrentHashMap<>();

    @Override
    @SneakyThrows
    public Post getPostById(Integer id) {
        Post postFromCache = postCache.get(id);
        if (postFromCache != null) {
            return postFromCache;
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found..."));

        TimeUnit.SECONDS.sleep(2);

        postCache.put(id, post);
        return post;
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
        postCache.remove(id);
    }

    @Override
    public void updateById(PostUpdater updater) {
        Post post = (Post) get(updater.id());

        post.setUserId(updater.userId());
        post.setTitle(updater.title());
        post.setBody(updater.body());
        Post save = postRepository.save(post);
        postCache.put(save.getId(), save);
    }
}
