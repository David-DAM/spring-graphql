package com.davinchicoder.springgraphql.exception;

public class PostNotFound extends RuntimeException {
    public PostNotFound() {
        super("Post not found");
    }
}
