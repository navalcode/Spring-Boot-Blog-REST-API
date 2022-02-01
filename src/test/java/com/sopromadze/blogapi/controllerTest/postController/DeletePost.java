package com.sopromadze.blogapi.controllerTest.postController;

import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
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
public class DeletePost {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostServiceImpl postService;


    //Test: Comproabr que devuelve 200
    //Entrada: idAlbum
    //Salida esperada: status Ok
    @DisplayName("Delete post with a user rol or admin rol")
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_USER"})
    void deletePost_return200() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }


    //Test: Comprobar que devuelve un 403 porque el usuario no tiene permisos
    //Entrada: idAlbum
    //Salida esperada: 403
    @DisplayName("Delete post without authorities")
    @Test
    void deletePost_return403() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1L)
                .contentType("application/json"))
                .andExpect(status().isForbidden());
    }
}
