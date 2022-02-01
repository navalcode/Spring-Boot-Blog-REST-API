package com.sopromadze.blogapi.controllerTest.categoryController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.service.CategoryService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
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
public class AddCategory {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;


    //Test: comprobar que devuelve un 200
    //entrada: post("/api/categories")
    //salida esperada: el test funciona correctamente
    @DisplayName("add category with a user user")
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void addCategory_return200() throws Exception{
        Category category = new Category();

        mockMvc.perform(post("/api/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());
    }
    //Test: comprobar que devuelve un 403
    //entrada: post("/api/categories")
    //salida esperada: el test funciona correctamente
    @DisplayName("add category without authorities")
    @Test
    void addCategory_return403() throws Exception{
        Category category = new Category();
        mockMvc.perform(post("/api/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isForbidden());
    }
    //Test: comprobar que devuelve un 400
    //entrada: post("/api/categories")
    //salida esperada: el test funciona correctamente
    @DisplayName("add category without content")
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void addCategory_return400() throws Exception{
        Category category = new Category();

        mockMvc.perform(post("/api/categories")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
