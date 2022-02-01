package com.sopromadze.blogapi.controllerTest.albumController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Log
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class GetAllPhotoByAlbum {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhotoServiceImpl photoService;


    /*
    Test:               Petición de obtener todos las fotos de un album
    Entrada:            get("/api/albums")
    Salida esperada:    Test exitoso, codigo de respuesta correcto
    */
    @DisplayName ("Get all photo by album response code 200")
    @Test
    void GetAllPhotoByAlbum_successWhenAccepted() throws Exception {
        PagedResponse<PhotoResponse> response= new PagedResponse<>();
        when(photoService.getAllPhotosByAlbum(1L, 1, 1)).thenReturn(response);

        mockMvc.perform(get("/api/albums/{id}/photos",1L)
                        .contentType("application/json")
                        .param("size", "1").param("page","1"))
                .andExpect(status().isOk());

    }


}
