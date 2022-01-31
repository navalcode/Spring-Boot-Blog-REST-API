package com.sopromadze.blogapi.controllerTest.authController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.payload.LoginRequest;
import com.sopromadze.blogapi.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class AuthenticateUser {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    /*Test: Check if authenticateUser returns 200 and jwt token
     * Input:LoginRequest
     * Output:ResponseEntity<JwtAuthenticationResponse> is ok
     * */
    @Test
    @DisplayName("Should return 200 when user is authenticated")
    @WithUserDetails(value = "user")
    public void authenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("user");
        loginRequest.setPassword("user");
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

        mockMvc.perform(post("/api/auth/signin")
                .contentType("application/json")
                .content(loginRequestJson))
                .andExpect(status().isOk());
    }

}
