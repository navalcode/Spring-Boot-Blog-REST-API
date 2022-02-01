package com.sopromadze.blogapi.controllerTest.albumController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.controller.AlbumController;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
//@WebMvcTest(controllers = AlbumController.class)
@AutoConfigureMockMvc
public class GetAllAlbums {

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
    Test:               Petición de obtener todos los albums
    Entrada:            get("/api/albums")
    Salida esperada:    Test exitoso, codigo de respuesta correcto
    */
    @DisplayName("Get all album response code 200")
    @Test
    //@WithUserDetails("admin")
    void GetAllAlbums_successWhenAccepted() throws Exception {

         mockMvc.perform(get("/api/albums")
                        .contentType("application/json")
                        .param("size", "1").param("page","1")
                        .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(result1element)))
                                )
                .andExpect(status().isOk());


    }


}
