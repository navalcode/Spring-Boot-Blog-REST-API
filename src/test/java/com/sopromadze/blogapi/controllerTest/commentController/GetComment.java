package com.sopromadze.blogapi.controllerTest.commentController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.CommentService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class GetComment {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;


    //Test: comprobar que devuelve un 200
    //entrada: get("/api/posts/{postId}/comments/{id}",1L,1L
    //salida esperada: el test funciona correctamente
    @DisplayName("get comment")
    @Test
    void getComment_return200() throws Exception{

        mockMvc.perform(get("/api/posts/{postId}/comments/{id}",1L,1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());

    }
}
