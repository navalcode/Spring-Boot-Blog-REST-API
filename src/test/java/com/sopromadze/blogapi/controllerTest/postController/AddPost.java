package com.sopromadze.blogapi.controllerTest.postController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.controllerTest.ResponseBodyMatchers;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.sopromadze.blogapi.controllerTest.ResponseBodyMatchers;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class AddPost {

    static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PostService postService;

        PostRequest postRequest;
        Category category;

        @BeforeEach
        void init() {
            category = new Category();
            category.setId(1L);
            category.setName("Categoria Guapa");

            postRequest = new PostRequest();
            postRequest.setTitle("Vaya titulo mas guapo");
            postRequest.setBody("tengo que rellenar 50 caracteres para que pase la validación por favor que alguien me ayude a escribir tanto");
            postRequest.setCategoryId(1L);
        }

        //Test: comprobar que devuelve un 201
        //entrada: post("/api/posts")
        //salida esperada: el test funciona correctamente
        @DisplayName("add post")
        @Test
        @WithMockUser(authorities = {"ROLE_USER"})
        void addPost_return201() throws Exception {

            mockMvc.perform(post("/api/posts")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(postRequest)))
                    .andExpect(status().isCreated());
        }

        //Test: comprobar que devuelve un 400
        //entrada: post("/api/posts")
        //salida esperada: el test funciona correctamente
        @DisplayName("add post without content")
        @Test
        @WithMockUser(authorities = {"ROLE_USER"})
        void addPost_return400() throws Exception {

            mockMvc.perform(post("/api/posts")
                            .contentType("application/json"))
                    .andExpect(status().isBadRequest());
        }

        //Test: comprobar que devuelve un 403
        //entrada: post("/api/posts")
        //salida esperada: el test funciona correctamente
        @DisplayName("add post without authorities")
        @Test
        @WithMockUser(authorities = {"ROLE_ADMIN"})
        void addPost_return403() throws Exception {

            mockMvc.perform(post("/api/posts")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(postRequest)))
                    .andExpect(status().isForbidden());
        }

        //Test: comprobar que devuelve un fallo de la validacion en el titulo del post
        //entrada: post("/api/posts")
        //salida esperada: el test funciona correctamente
        @DisplayName("add post with validation error")
        @Test
        @WithMockUser(authorities = {"ROLE_USER"})
        void addPost_returns() throws Exception {
            postRequest.setTitle("hola");


            MvcResult mvcResult = mockMvc.perform(post("/api/posts")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(postRequest)))
                    .andExpect(status().isBadRequest()).andReturn();

            String actualResponseBody =
                    mvcResult.getResponse().getContentAsString();
            String[] actualTimeStamp = mvcResult.getResponse().getContentAsString().split("\"");

            ResponseBodyMatchers.ErrorResult expectedErrorResponse = new ResponseBodyMatchers.ErrorResult();
            expectedErrorResponse.setTimestamp(actualTimeStamp[13]);
            expectedErrorResponse.setError("Bad Request");
            expectedErrorResponse.setMessages(List.of("title - size must be between 10 and 2147483647"));
            expectedErrorResponse.setStatus(400);


            String expectedResponseBody =
                    objectMapper.writeValueAsString(expectedErrorResponse);
            assertThat(actualResponseBody)
                    .isEqualToIgnoringWhitespace(expectedResponseBody);
        }

}
