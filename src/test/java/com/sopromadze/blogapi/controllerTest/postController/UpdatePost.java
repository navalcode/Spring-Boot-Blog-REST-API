package com.sopromadze.blogapi.controllerTest.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class UpdatePost {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostServiceImpl postService;

    private Post post;
    private PostRequest postRequest;

    @BeforeEach
    void init(){
        //Create post
        post = new Post();
        post.setId(1L);
        post.setUpdatedAt(Instant.now());
        post.setTitle("title with more than 10 characters");
        post.setBody("body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,");

        //Create postRequest
        postRequest = new PostRequest();
        postRequest.setTitle("title with more than 10 characters");
        postRequest.setBody("body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,body with more than 50 characters,");
        postRequest.setCategoryId(1L);

    }

    /*Test:Check if update post returns 200 when updated
    * Input:PostId, PostRequest, UserPrincipal
    * Output:ResponseEntity<Post> with status 201 created
    * */
    @Test
    @DisplayName("Controller update post success")
    @WithUserDetails("user")
    void updatePost_success() throws Exception {

        String postJson = objectMapper.writeValueAsString(postRequest);

        when(postService.updatePost(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenReturn(post);
        MvcResult mvcResult = mockMvc.perform(put("/api/posts/{id}", 1L)
                        .contentType("application/json")
                        .content(postJson))
                .andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(postRequest);

        Post expected = objectMapper.readValue(expectedJson, Post.class);
        PostRequest actualPostRequest = objectMapper.readValue(actual, PostRequest.class);

        assertTrue(actualPostRequest.getTitle().equals(expected.getTitle()));
        assertTrue(actualPostRequest.getBody().equals(expected.getBody()));
    }
    /*Test:Check if update Post throws 401 when user is not logged in
    * Input:PostId, PostRequest
    * Output:ResponseEntity<Post> with status 401 unauthorized
    * */
    @Test
    @DisplayName("Controller update post unauthorized")
    void updatePost_unauthorized() throws Exception {

        String postJson = objectMapper.writeValueAsString(postRequest);

        when(postService.updatePost(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenReturn(post);
        MvcResult mvcResult = mockMvc.perform(put("/api/posts/{id}", 1L)
                .contentType("application/json")
                .content(postJson))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    /*Test:Check if update Post throws 404 when post is not found
    * Input:PostId, PostRequest
    * Output:ResponseEntity<Post> with status 404 not found
    * */
    @Test
    @DisplayName("Controller update post not found")
    @WithUserDetails("user")
    void updatePost_notFound() throws Exception {

        String postJson = objectMapper.writeValueAsString(postRequest);

        when(postService.updatePost(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenThrow(new ResourceNotFoundException("Post","id", 1L));
        MvcResult mvcResult = mockMvc.perform(put("/api/posts/{id}", 1L)
                .contentType("application/json")
                .content(postJson))
                .andExpect(status().isNotFound()).andReturn();
    }

    /*Test:Check if update Post throws 400 when the request is invalid
    * Input:PostId, wrong PostRequest
    * Output:ResponseEntity<Post> with status 400 bad request
    * */
    @Test
    @DisplayName("Controller update post bad request")
    @WithUserDetails("user")
    void updatePost_badRequest() throws Exception {

        String postJson = "Not what the controller expects";

        MvcResult mvcResult = mockMvc.perform(put("/api/posts/{id}", 1L)
                .contentType("application/json")
                .content(postJson))
                .andExpect(status().isBadRequest()).andReturn();
    }

}
