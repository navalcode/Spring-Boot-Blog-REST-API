package com.sopromadze.blogapi.controllerTest.userController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest (classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TakeAdmin {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;



    /*
     Test:               Petición para quitar el rol de administrador a un usuario
     Entrada:            put("/api/users/{username}/takeAdmin","Pepe777")
     Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
     */
    @Test
    @WithMockUser (authorities = {"ROLE_ADMIN"})
    @DisplayName ("Take admin successfully")
    void takeAdmin_success() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        rolesUser.add(new Role(RoleName.ROLE_ADMIN));
        user.setRoles(rolesUser);

        ApiResponse response = new ApiResponse();
        when(userService.removeAdmin(user.getUsername())).thenReturn(response);

        mockMvc.perform(put("/api/users/{username}/takeAdmin","Pepe777")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());

    }


    /*
    Test:               Petición para mostrar al usuario registrado fallido por falta de autorizacion
    Entrada:            put("/api/users/{username}/takeAdmin","Pepe777")
    Salida esperada:    Test exitoso, codigo de respuesta correcto (403)
    */
    @Test
    @WithMockUser (authorities = {"ROLE_USER"}) // o sin autorización
    @DisplayName ("Error code 403 Take admin")
    void takeAdmin_successWhen403() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);

        ApiResponse response = new ApiResponse();
        when(userService.removeAdmin(user.getUsername())).thenReturn(response);

        mockMvc.perform(put("/api/users/{username}/takeAdmin","Pepe777")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

    }
}
