package com.sopromadze.blogapi.controllerTest.tagController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.service.TagService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AddTag {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    Tag tag;
    @BeforeEach
    void init(){
        tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag Numero 1");
    }
    //Test: Comprobar que devuelve 201
    //Entrada: post("/api/tags")
    //Salida esperada: El test se realiza con exito y devuelve 201
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void addTag_success() throws Exception{

        mockMvc.perform(post("/api/tags")
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isCreated());
    }
    //Test: Comprobar que devuelve 403
    //Entrada: post("/api/tags")
    //Salida esperada: El test se realiza con exito y devuelve 403
    @Test
    void addTag_return403() throws Exception{

        mockMvc.perform(post("/api/tags")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isForbidden());
    }
}
