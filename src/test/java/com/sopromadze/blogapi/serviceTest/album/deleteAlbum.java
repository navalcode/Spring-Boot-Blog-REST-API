package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class deleteAlbum {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    AlbumServiceImpl albumService;

    /* Test: Comprobar que borra un album
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: un código 200, de haber borrado exitosamente el álbum */
    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void deleteAlbum_success(){
        //User creation
        User u = new User(
                "Frenando",
                "Alonso",
                "frenandoalonso20",
                "frenandoalonso20@gmail.com",
                "1234"); //Meter rol admin o no funciona
        u.setId(1L);

        //UserPrincipal creation
        UserPrincipal userPrincipal = new UserPrincipal(
                1L,
                "Frenando",
                "Alonso",
                "frenandoalonso20",
                "frenandoalonso20@gmail.com",
                "1234",
                Collections.emptyList()
        );

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");

        Album a =new Album();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");

        a.setUser(userRepository.getUser(userPrincipal));

        when(albumRepository.save(a)).thenReturn(a);

        albumService.addAlbum(a1, userPrincipal);

        assertEquals(HttpStatus.OK, albumService.deleteAlbum(1L,userPrincipal).getStatusCode());

    }
}
