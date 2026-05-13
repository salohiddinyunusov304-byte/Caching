package uz.pdp.caching.service;

import uz.pdp.caching.entity.Post;
import uz.pdp.caching.payload.PostUpdater;

public interface PostService {
    Post getPostById(Integer id);

    void deleteById(Integer id);

    void updateById(PostUpdater updater);
}
