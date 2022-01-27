package com.sopromadze.blogapi.serviceTest.album;


import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class AddAlbum {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    AlbumServiceImpl albumService;

    Album album;
    AlbumRequest albumRequest;
    UserPrincipal userPrincipal;

    @BeforeEach
    void InitTest(){
        userPrincipal = new UserPrincipal(1L,"Pepe","Garcia", "Sony777","sony777@gmail.com","1234", Collections.emptyList());
        album = new Album();
        albumRequest= new AlbumRequest();
    }

    /*
        Test:               Comprobar se añade un usuario a un nuevo album
        Entrada:            albumService.addAlbum(albumRequest,userPrincipal)
        Salida esperada:    El test se realiza con exito
    */
    @DisplayName("add Album to User")
    @Test
    void addAlbum_Success() {
        User user = new User("Pepe","Garcia", "Sony777","sony777@gmail.com","1234");
        user.setId(1L);

        UserPrincipal userPrincipal = new UserPrincipal(1L,"Pepe","Garcia", "Sony777","sony777@gmail.com","1234", Collections.emptyList());
        when(userRepository.getUserByName(userPrincipal.getUsername())).thenReturn(user);

        album.setUser(userRepository.getUser(userPrincipal));
        when(albumRepository.save(album)).thenReturn(album);

        assertEquals(albumService.addAlbum(albumRequest,userPrincipal),album);

    }

    /*
        Test:               Comprobar se añade un usuario a un nuevo album
        Entrada:            albumService.addAlbum(albumRequest,userPrincipal)
        Salida esperada:    La respuesta deberia ser nula siendo el usuarioPrincipal nulo
    */
    @DisplayName("Not added Album to User (null)")
    @ParameterizedTest
    @NullSource
    void addAlbum_SuccessWhenNull(UserPrincipal userPrincipal) {

        User user = new User();
        album.setUser(user);
        assertNull(albumService.addAlbum(albumRequest,userPrincipal));

    }
}
