package com.sopromadze.blogapi.controllerTest.photoController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.PhotoService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class GetPhoto {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    //Test: comprobar que devuelve un 200
    //entrada: get("/api/photos/{id}",1
    //salida esperada: el test funciona correctamente
    @DisplayName("get photo")
    @Test
    void getPhoto_return200() throws Exception{

        mockMvc.perform(get("/api/photos/{id}",1)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
