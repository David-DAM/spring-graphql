package com.davinchicoder.springgraphql.mapper;

import com.davinchicoder.springgraphql.dto.PostDto;
import com.davinchicoder.springgraphql.entity.Post;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PostMapper implements Function<PostDto, Post> {

    @Override
    public Post apply(PostDto postDto) {
        return Post.builder()
                .title(postDto.getTitle())
                .author(postDto.getAuthor())
                .content(postDto.getContent())
                .imageUrl(postDto.getImageUrl())
                .build();
    }
}
