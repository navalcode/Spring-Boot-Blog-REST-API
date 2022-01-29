package com.sopromadze.blogapi.controllerTest.albumController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.AlbumService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DeleteAlbum {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    //Test: comprobar que devuelve un 200
    //entrada: delete("/api/albums/{id}",1L
    //salida esperada: el test funciona correctamente
    @DisplayName("delete album with a user admin or a user user")
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN","ROLE_USER"})
    void deleteAlbum_return200() throws Exception {
        mockMvc.perform(delete("/api/albums/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
    //Test: comprobar que devuelve un 403 porque el usuario no tiene permisos
    //entrada: delete("/api/albums/{id}",1L
    //salida esperada: el test funciona correctamente
    @DisplayName("delete album without authorities")
    @Test
    void deleteAlbum_return403() throws Exception {
        mockMvc.perform(delete("/api/albums/{id}",1L)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }
}
