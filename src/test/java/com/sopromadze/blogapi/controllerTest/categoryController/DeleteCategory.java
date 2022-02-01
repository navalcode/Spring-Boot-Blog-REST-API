package com.sopromadze.blogapi.controllerTest.categoryController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DeleteCategory {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @DisplayName("delete category with role user or role admin")
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void deleteCategory_return200() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @DisplayName("Delete category without authorities")
    @Test
    void deleteCategory_return403() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isForbidden());
    }
}
