package com.sopromadze.blogapi.controllerTest.userController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.PostService;
import lombok.extern.java.Log;
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
public class GetPostsCreatedBy {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    //Test: Comprobar que devuelve 200
    //Entrada: get("/api/users/{username}/posts","Alejandro")
    //Salida esperada: El test se realiza con exito y devuelve 200
    @Test
    void getPostsCreatedBy_success() throws Exception{

        mockMvc.perform(get("/api/users/{username}/posts","Alejandro")
                .contentType("application/json")
                .param("page","1")
                .param("size","1"))
                .andExpect(status().isOk());
    }
}
