package com.sopromadze.blogapi.controllerTest.userController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.InfoRequest;
import com.sopromadze.blogapi.payload.UserProfile;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class SetAddress {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private UserProfile user;
    private InfoRequest infoRequest;

    @BeforeEach
    void init(){
        user = new UserProfile();
        user.setUsername("username");
        user.setId(2L);
        user.setLastName("lastname");
        user.setFirstName("user");
        user.setEmail("user@user.com");

        infoRequest = new InfoRequest();
        infoRequest.setCity("Seville");
        infoRequest.setStreet("Condes");
        infoRequest.setZipcode("41013");
        infoRequest.setSuite("Suite");


    }

    /*Test:Check if Controller add user works
     * Input:User
     * Output:ResponseEntity<User> with status 200 ok
     * */
    @Test
    @DisplayName("Add user success")
    @WithUserDetails("admin")
    void addUser_success() throws Exception{
        when(userService.setOrUpdateInfo(Mockito.any(),Mockito.any())).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/setOrUpdateInfo")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(infoRequest)))
                .andExpect(status().isOk()).andReturn();

        User actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),User.class);

        assertTrue(actual.getUsername().equals(user.getUsername()));
    }

    /*Test:Check if Controller add user throws unauthorized
     * Input:User
     * Output:ResponseEntity<User> with status 401 unauthorized
     * */
    @Test
    @DisplayName("Add user unauthorized")
    void addUser_unauthorized() throws Exception{
        when(userService.setOrUpdateInfo(Mockito.any(),Mockito.any())).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/setOrUpdateInfo")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(infoRequest)))
                .andExpect(status().isUnauthorized()).andReturn();

    }

}
