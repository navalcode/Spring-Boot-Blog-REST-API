package com.sopromadze.blogapi.controllerTest.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.service.PostService;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetPost {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;


    /*
    Test:               Petición de obtener un post
    Entrada:            get("/api/posts/{id}/",any(Long.class))
    Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
    */
    @DisplayName("Get post")
    @Test
    void getPost_return200() throws Exception{

        Post post = new Post();
        post.setId(1L);

        when(postService.getPost(1L)).thenReturn(post);

        mockMvc.perform(get("/api/posts/{id}" ,1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
