package com.sopromadze.blogapi.controllerTest.userController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.UserService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GiveAdmin {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    //Test: Comprobar que devuelve 200
    //Entrada: get("/api/users/{username}/giveAdmin","Alejandro")
    //Salida esperada: El test se realiza con exito y devuelve 200
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void giveAdmin_success() throws Exception{
        mockMvc.perform(put("/api/users/{username}/giveAdmin","Alejandro")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    //Test: Comprobar que devuelve 403
    //Entrada: get("/api/users/{username}/giveAdmin","Alejandro")
    //Salida esperada: El test se realiza con exito y devuelve 403
    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void giveAdmin_return403() throws Exception{
        mockMvc.perform(put("/api/users/{username}/giveAdmin","Alejandro")
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }
}
