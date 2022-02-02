package com.sopromadze.blogapi.controllerTest.categoryController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.service.CategoryService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetCategory {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    /*
    Test:               Petición de obtener una categoria
    Entrada:            get("/api/categories/{id}/",any(Long.class))
    Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
    */
    @DisplayName("Get category return 200")
    @Test
    void getCategory_return200() throws Exception{
        mockMvc.perform(get("/api/categories/{id}/",any(Long.class))
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }


    /*
    Test:               Petición de obtener una categoria no encontrada
    Entrada:            get("/api/categories/{id}/",any(Long.class))
    Salida esperada:    Test exitoso, codigo de respuesta correcto (404)
    */
    @DisplayName("Get category return 404")
    @Test
    void getCategory_return404() throws Exception{
        when(categoryService.getCategory(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Category","id", 1L));

        mockMvc.perform(get("/api/categories/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }




}
