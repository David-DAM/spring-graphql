package com.davinchicoder.springgraphql.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {

    private String title;
    private String content;
    private String author;
    private String imageUrl;
}
