package uz.pdp.caching.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.pdp.caching.entity.Post;
import uz.pdp.caching.payload.PostUpdater;
import uz.pdp.caching.service.PostService;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/findById/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteById(@PathVariable Integer id) {
        postService.deleteById(id);
    }

    @PutMapping("/updateById")
    private void updateById(@RequestBody PostUpdater updater) {
        postService.updateById(updater);
    }
}
