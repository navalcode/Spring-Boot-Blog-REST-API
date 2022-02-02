package com.sopromadze.blogapi.controllerTest.userController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.payload.UserIdentityAvailability;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class CheckUserNameAvailability {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    private UserIdentityAvailability userIdentityAvailability;

    @BeforeEach
    void init(){
        userIdentityAvailability= new UserIdentityAvailability(true);

    }

    /*
    * Test:Check if UsernameAvailability works
    * Input: non-existent username
    * Output:ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
    * */
    @Test
    @DisplayName("User controller check username availability works")
    @WithUserDetails("user")
    void checkUsernameAvailability_success() throws Exception{
        String username = "username";

        when(userService.checkUsernameAvailability(Mockito.anyString())).thenReturn(userIdentityAvailability);

        mockMvc.perform(get("/api/users/checkUsernameAvailability")
                        .contentType("application/json")
                        .param("username",username))
                .andExpect(status().isOk());
    }
}
