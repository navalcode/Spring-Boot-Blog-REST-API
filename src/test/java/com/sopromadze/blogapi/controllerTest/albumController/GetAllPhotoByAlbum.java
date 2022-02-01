package com.sopromadze.blogapi.controllerTest.albumController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

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
    private UserRepository userRepository;


    @MockBean
    private AlbumServiceImpl albumService;


    PagedResponse<AlbumResponse> resultadoEsperado;
    Page result1element;

    @BeforeEach
    void initTest() {
        UserPrincipal userPrincipal = new UserPrincipal(1L,"Pepe","Garcia", "Sony777","sony777@gmail.com","1234", Collections.emptyList());
        Album album = new Album();
        album.setUser(userRepository.getUser(userPrincipal));

        album.setTitle("Título");
        album.setId(1L);
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.setUser(userRepository.getUser(userPrincipal));
        albumResponse.setId(1L);
        albumResponse.setTitle("Título");

        List<AlbumResponse> resultado2 = Collections.singletonList(albumResponse);

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(resultado2);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

        result1element = new RestResponsePage(List.of(resultadoEsperado));


    }

    /*
    Test:               Petición de obtener todos las fotos de un album
    Entrada:            get("/api/albums")
    Salida esperada:    Test exitoso, codigo de respuesta correcto
    */
    @DisplayName ("Get all photo by album response code 200")
    @Test
    void GetAllPhotoByAlbum_successWhenAccepted() throws Exception {
        mockMvc.perform(get("/api/albums/{id}/photos",1L)
                        .contentType("application/json")
                        .param("size", "1").param("page","1"))
                .andExpect(status().isOk());


    }

}
