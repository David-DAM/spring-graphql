package com.davinchicoder.springgraphql.controller;

import com.davinchicoder.springgraphql.dto.PostDto;
import com.davinchicoder.springgraphql.entity.Post;
import com.davinchicoder.springgraphql.exception.PostNotFound;
import com.davinchicoder.springgraphql.mapper.PostMapper;
import com.davinchicoder.springgraphql.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @QueryMapping
    public List<Post> getRecentPosts(@Argument int count, @Argument int offset) {
        return postRepository.getRecentPosts(count, offset);
    }

    @QueryMapping
    public Post getPostById(@Argument Long id) {
        return postRepository.getById(id).orElseThrow(PostNotFound::new);
    }

    @QueryMapping
    public List<Post> getAllPosts() {
        return postRepository.getAll();
    }

    @MutationMapping
    public Post deletePostById(@Argument Long id) {

        return postRepository.delete(id).orElseThrow(PostNotFound::new);
    }

    @MutationMapping
    public Post savePost(@Argument PostDto postDto) {
        Post post = postMapper.apply(postDto);
        return postRepository.save(post);
    }
}
