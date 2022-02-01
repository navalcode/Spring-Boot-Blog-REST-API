package com.sopromadze.blogapi.controllerTest.userController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.UserService;
import lombok.extern.java.Log;
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
public class CurrentUser {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;



    /*
     Test:               Petición para mostrar al usuario registrado
     Entrada:            get("/api/users/me")
     Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
     */
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void currentUser_success() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        UserSummary result= new UserSummary(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName());

        when(userService.getCurrentUser(userP)).thenReturn(result);

        mockMvc.perform(get("/api/users/me")
                        .contentType("application/json"))
                .andExpect(status().isOk());


    }


    /*
    Test:               Petición para mostrar al usuario registrado fallido por falta de autorizacion
    Entrada:            get("/api/users/me")
    Salida esperada:    Test exitoso, codigo de respuesta correcto (403)
    */
    @Test
    void currentUser_successWhen403() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        UserSummary result= new UserSummary(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName());

        when(userService.getCurrentUser(userP)).thenReturn(result);

        mockMvc.perform(get("/api/users/me")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

    }


    /*
Test:               Petición para mostrar al usuario registrado fallido por error en la ruta
Entrada:            get("/api/users/yo")
Salida esperada:    Test exitoso, codigo de respuesta correcto (400)
*/

    /*
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void currentUser_successWhen400() throws Exception{

        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        UserSummary result= new UserSummary(user.getId(),user.getUsername(),user.getFirstName(),user.getLastName());

        when(userService.getCurrentUser(userP)).thenReturn(result);

        mockMvc.perform(get("/api/users/me")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }

     */





}
