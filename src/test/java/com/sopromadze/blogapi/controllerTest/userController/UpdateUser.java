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

    @MockBean
    private UserServiceImpl userService;

    User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(1L);
        user.setFirstName("FirstName");
        user.setEmail("Email@gmail.com");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setUsername("Username");
    }

    @Test
    @DisplayName("Controller update user success")
    @WithMockUser(authorities = {"ROLE_USER","ROLE_ADMIN"})
    void updateUser_success() throws Exception {
        ResponseEntity<User> expected = ResponseEntity.ok(user);
        String userJson = objectMapper.writeValueAsString(user);

        //when(userService.updateUser(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/{username}", "Username")
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expected.getBody());
        assertEquals(expectedJson, actual);
    }

}
