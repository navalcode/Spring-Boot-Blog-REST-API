package com.sopromadze.blogapi.controllerTest.todoController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.service.TodoService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class AddTodo {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TodoService todoService;

    Todo todo;
    @BeforeEach
    void init(){
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Todo para luego");
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());
    }
    //Test: Comprobar que devuelve 201
    //Entrada: post("/api/todos")
    //Salida esperada: El test se realiza con exito y devuelve 201
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void addTodo_success() throws Exception{

        mockMvc.perform(post("/api/todos")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated());
    }
    //Test: Comprobar que devuelve 401
    //Entrada: post("/api/todos")
    //Salida esperada: El test se realiza con exito y devuelve 401
    @Test
    void addTodo_return403() throws Exception{

        mockMvc.perform(post("/api/todos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isUnauthorized());
    }
    //Test: Comprobar que devuelve 400
    //Entrada: post("/api/todos")
    //Salida esperada: El test se realiza con exito y devuelve 400
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void addTodo_return400() throws Exception{

        mockMvc.perform(post("/api/todos")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}
