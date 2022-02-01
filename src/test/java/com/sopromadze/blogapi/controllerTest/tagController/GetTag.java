package com.sopromadze.blogapi.controllerTest.tagController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;

import com.sopromadze.blogapi.service.CategoryService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetTag {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    /*
    Test:               Petición de obtener un tag
    Entrada:            get("/api/tags/{id}/",any(Long.class))
    Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
    */
    @DisplayName("Get tag")
    @Test
    void getTag_return200() throws Exception{
        mockMvc.perform(get("/api/tags/{id}/",1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }




}