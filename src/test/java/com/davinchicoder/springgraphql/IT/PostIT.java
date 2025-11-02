package com.davinchicoder.springgraphql.IT;

import com.davinchicoder.springgraphql.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostIT {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldGetAllPosts() {
        String query = """
                query {
                    getAllPosts {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("data.getAllPosts")
                .entityList(Post.class)
                .hasSize(3);
    }

    @Test
    void shouldGetRecentPosts() {
        String query = """
                query {
                    getRecentPosts(count: 2, offset: 1) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("data.getRecentPosts")
                .entityList(Post.class)
                .hasSize(2)
                .satisfies( posts ->
                        posts.forEach(post ->
                                assertNotEquals(1, post.getId())
                        )
                );
    }

    @Test
    void shouldGetPostById() {
        String query = """
                query {
                    getPostById(id: 1) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("data.getPostById")
                .entity(Post.class)
                .satisfies(post -> {
                    assertNotNull(post.getId());
                    assertNotNull(post.getTitle());
                    assertNotNull(post.getContent());
                    assertNotNull(post.getAuthor());
                });
    }

    @Test
    void getPostByIdNotFound() {
        String query = """
                query {
                    getPostById(id: 9) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .errors()
                .satisfy(errors -> {
                    ResponseError error = errors.getFirst();
                    assertEquals("getPostById", error.getPath());
                    assertEquals("Post not found", error.getMessage());
                    assertNotNull(error.getLocations());
                    assertNotNull(error.getExtensions());
                });
    }

    @Test
    void shouldCreatePost() {
        String mutation = """
                mutation {
                    savePost(postDto: {
                        title: "Test Post",
                        content: "Test Content",
                        author: "Test Author",
                        imageUrl: "Test Image url",
                    }) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("data.savePost")
                .entity(Post.class)
                .satisfies(post -> {
                    assertNotNull(post.getId());
                    assertEquals("Test Post", post.getTitle());
                    assertEquals("Test Content", post.getContent());
                    assertEquals("Test Author", post.getAuthor());
                });
    }

    @Test
    void shouldDeletePostById() {
        String mutation = """
                mutation {
                    deletePostById(id: 1) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("data.deletePostById")
                .entity(Post.class)
                .satisfies(post -> {
                    assertEquals(1, post.getId());
                });
    }

    @Test
    void deletePostByIdNotFound() {
        String mutation = """
                mutation {
                    deletePostById(id: 9) {
                        id
                        title
                        content
                        author
                    }
                }
                """;

        graphQlTester.document(mutation)
                .execute()
                .errors()
                .satisfy(errors -> {
                    ResponseError error = errors.getFirst();
                    assertEquals("deletePostById", error.getPath());
                    assertEquals("Post not found", error.getMessage());
                    assertNotNull(error.getLocations());
                    assertNotNull(error.getExtensions());
                });
    }
}