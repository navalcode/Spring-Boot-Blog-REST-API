package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class deleteAlbum {

    @Autowired
    private TestEntityManager testEntityManager;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    AlbumServiceImpl albumService;

    /* Test: Comprobar que borra un album
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: un código 200, de haber borrado exitosamente el álbum */
    @Test
    void deleteAlbum_success(){
        //UserPrincipal creation
        UserPrincipal u = new UserPrincipal(
                5L,
                "Frenando",
                "Alonso",
                "frenandoalonso20",
                "frenandoalonso20@gmail.com",
                "1234",
                Collections.emptyList()); //Meter rol admin o no funciona

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");

        albumService.addAlbum(a1, u);

        testEntityManager.persist(u);
        testEntityManager.persist(a1);

        albumService.deleteAlbum(1L, u);

    }
}
