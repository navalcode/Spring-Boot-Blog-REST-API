package com.sopromadze.blogapi.controllerTest.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.payload.PostResponse;
import com.sopromadze.blogapi.service.PostService;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetPostsByCategory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostServiceImpl postService;

    @BeforeEach
    void init() {
        Post post = new Post();
        post.setId(1L);


    }

    @DisplayName("Get Post by Category response code 200")
    @Test
    void getPostsByCategory_return200() throws Exception {
        PagedResponse<Post> response = new PagedResponse<>();
        when(postService.getPostsByCategory(1L, 1, 1)).thenReturn(response);

        mockMvc.perform(get("/api/posts/category/{id}", 1L)
                .contentType("application/json")
                .param("size", "1").param("page", "1"))
                .andExpect(status().isOk());
    }
}
