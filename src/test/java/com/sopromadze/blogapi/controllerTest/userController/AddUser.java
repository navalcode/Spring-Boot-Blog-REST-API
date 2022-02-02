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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class AddUser {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void init(){
        user = new User();
        user.setUsername("username");
        user.setId(2L);
        user.setPassword("password");
        user.setLastName("lastname");
        user.setFirstName("user");
        user.setEmail("user@user.com");
    }

    /*Test:Check if Controller add user works
    * Input:User
    * Output:ResponseEntity<User> with status 201 created
    * */
    @Test
    @DisplayName("Add user success")
    @WithUserDetails("admin")
    void addUser_success() throws Exception{
        when(userService.addUser(Mockito.any())).thenReturn(user);

       MvcResult mvcResult = mockMvc.perform(post("/api/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated()).andReturn();

       User actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),User.class);

       assertTrue(actual.getUsername().equals(user.getUsername()));
    }

    /*Test:Check if Controller add user throws Forbidden exception
     * Input:User
     * Output:ResponseEntity<User> with status 403 Forbidden
     * */
    @Test
    @DisplayName("Controller add user fails with wrong authorization")
    @WithUserDetails("user")
    void addUser_failsWithWrongAurhorities() throws Exception {
        when(userService.addUser(Mockito.any())).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden()).andReturn();
    }
}
