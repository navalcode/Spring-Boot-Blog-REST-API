package com.sopromadze.blogapi.controllerTest.todoController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;

import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.TodoService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetTodo {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    /*
    Test:               Petición de obtener un todo
    Entrada:            get("/api/todos/{id}",1L)
    Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
    */
    @DisplayName("Get todo return 200")
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void getTag_return200() throws Exception{

        Todo todo = new Todo();
        todo.setId(1L);
        User user = new User();
        user.setId(1L);
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);

        when(todoService.getTodo(todo.getId(),userP)).thenReturn(todo);

        mockMvc.perform(get("/api/todos/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /*
    Test:               Petición para comprobar que devuelve 403 sin autorizacion
    Entrada:            get("/api/todos/{id}",1L)
    Salida esperada:    Test exitoso, codigo de respuesta correcto (403)
    */
    @DisplayName("Get todo return 403")
    @Test
    void getTag_return403() throws Exception{

        Todo todo = new Todo();
        todo.setId(1L);
        User user = new User();
        user.setId(1L);
        List<Role> rolesUser = new ArrayList<Role>();
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);

        when(todoService.getTodo(todo.getId(),userP)).thenReturn(todo);

        mockMvc.perform(get("/api/todos/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }





}