package com.sopromadze.blogapi.controllerTest.categoryController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class UpdateCategory {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryServiceImpl categoryService;

    Category category;


    @BeforeEach
    void init(){
        //Create category
        category = new Category();
        category.setId(1L);
        category.setName("category");
        category.setCreatedBy(1L);

    }

    /*Test:Check if update category returns 201 when updated
    * Input:categoryId , UserPrincipal
    * Output:ResponseEntity<Category>
    * */
    @Test
    @DisplayName("Controller update category success")
    @WithUserDetails("user")
    void updateCategory_success() throws Exception {

        ResponseEntity<Category> expected = ResponseEntity.ok(category);
        String categoryJson = objectMapper.writeValueAsString(category);

        when(categoryService.updateCategory(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenReturn(expected);

       MvcResult mvcResult= mockMvc.perform(put("/api/categories/{id}",1L)
                .contentType("application/json")
                .content(categoryJson))
                .andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expected.getBody());
        assertEquals(expectedJson, actual);
    }

    /*Test:Check if updateCategory throws 401 when ther is no user
    * Input:CategoryId,no User
    * Output:Throws Unauthorized Exception
    * */
    @Test
    @DisplayName("Controller update category unauthorized")
    void updateCategory_unauthorized() throws Exception {

        String categoryJson = objectMapper.writeValueAsString(category);

        mockMvc.perform(put("/api/categories/{id}",1L)
                .contentType("application/json")
                .content(categoryJson))
                .andExpect(status().isUnauthorized());
    }

    /*Test:Check if updateCategory throws 404 when categoryId is not found
    * Input:CategoryId,UserPrincipal
    * Output:Throws NotFound Exception
    * */
    @Test
    @DisplayName("Controller update category not found")
    @WithUserDetails("user")
    void updateCategory_notFound() throws Exception {

        String categoryJson = objectMapper.writeValueAsString(category);
        when(categoryService.updateCategory(Mockito.anyLong(), Mockito.any(),Mockito.any())).thenThrow(new ResourceNotFoundException("Category","id",1L));

        mockMvc.perform(put("/api/categories/{id}",1L)
                .contentType("application/json")
                .content(categoryJson))
                .andExpect(status().isNotFound());
    }

    /*Test:Check if updateCategory throws 400 when RequestBody is wrong
    * Input:CategoryId,UserPrincipal
    * Output:Throws BadRequest Exception
    * */
    @Test
    @DisplayName("Controller update category bad request")
    @WithUserDetails("user")
    void updateCategory_badRequest() throws Exception {

        String categoryJson = "Not a real json";

        mockMvc.perform(put("/api/categories/{id}",1L)
                .contentType("application/json")
                .content(categoryJson))
                .andExpect(status().isBadRequest());
    }

}


