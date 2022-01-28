package com.sopromadze.blogapi.controllerTest.albumController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.user.User;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.ResultActions.*;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetAlbum {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumServiceImpl albumService;

    Album album;
    Page result;

    @BeforeEach
    void initTest() {

        album = new Album();
        album.setId(1L);
        result = new RestResponsePage(List.of(album));
    }

    @DisplayName("Get album response code 200")
    @Test
    void getAlbum_success() throws Exception{
        ResponseEntity<Album> result = new ResponseEntity<>(album, HttpStatus.OK);
        when(albumService.getAlbum(1L)).thenReturn(result);

        mockMvc.perform(get("/api/albums")
                .contentType("application/json")
                .param("id", "1")
                .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(result))))
                .andExpect(status().isOk());

    }
    
}
