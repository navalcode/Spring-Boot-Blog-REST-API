package com.sopromadze.blogapi.controllerTest.categoryController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.CategoryService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class DeleteCategory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    Category category;

    @BeforeEach
    void init() {
        category = new Category();
        category.setId(1L);

    }

    @Test
    @WithUserDetails("admin")
    void deleteCategory_success() throws Exception {
        User user = new User();
        user.setId(1L);

        ResponseEntity<Category> result = new ResponseEntity<>(category, HttpStatus.OK);
        when(categoryService.getCategory(1L)).thenReturn(result);


        mockMvc.perform(delete("/api/category/{id}")
                .contentType("application/json")
                        .param("id", "1")
                        .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(result))))

                .andExpect(status().isCreated());
    }
}
