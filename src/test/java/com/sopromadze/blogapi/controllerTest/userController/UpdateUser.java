package com.sopromadze.blogapi.controllerTest.userController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class UpdateUser {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserServiceImpl userService;

    User user;

    @BeforeEach
    void init() {
        user = new User();

        user.setFirstName("user");
        user.setEmail("user@gmail.com");
        user.setLastName("user");
        user.setPassword("user");
        user.setUsername("user");
    }

    @Test
    @DisplayName("Controller update user success")
    @WithUserDetails("user")
    void updateUser_success() throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        when(userService.updateUser(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/{username}", "user")
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isCreated()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(user);
        assertEquals(expectedJson, actual);
    }

}
