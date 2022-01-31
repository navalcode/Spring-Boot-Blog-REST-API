package com.sopromadze.blogapi.controllerTest.photoController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class GetAllPhotos {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Photo photo;
    PagedResponse<PhotoResponse> resultadoEsperado;
    RestResponsePage result1element;

    @BeforeEach
    void init() {

        User user = new User("Pepe","Garcia", "Sony777","sony777@gmail.com","1234");
        user.setId(1L);
        UserPrincipal userPrincipal = new UserPrincipal(1L,"Pepe","Garcia", "Sony777","sony777@gmail.com","1234", Collections.emptyList());
        Album album = new Album();
        photo = new Photo();
        photo.setTitle("Titulo");
        photo.setUrl("Url");
        photo.setThumbnailUrl("ThumnailUrl");
        photo.setAlbum(album);

        PhotoResponse photoResponse = new PhotoResponse(1l, "Titulo", "Url", "ThumbnailUrl", 1L);

        List<PhotoResponse> resultado2 = Collections.singletonList(photoResponse);

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(resultado2);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

        result1element = new RestResponsePage<>(List.of(resultadoEsperado));
    }

    @DisplayName("Get all photos response code 200")
    @Test
    void GetAllPhotos_succesWhenAccepted() throws Exception {
        mockMvc.perform(get("/api/photos")
                .contentType("application/json")
                .param("size", "1").param("page", "1")
                .content(objectMapper.writeValueAsString(result1element)))
                .andExpect(status().isOk());
    }
}
