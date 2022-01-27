package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserAlbums {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    private User user;
    private Album album;
    private Page<Album> albums;
    private PagedResponse<Album> resultado;

    @BeforeEach
    void init(){
        user = new User();
        user.setId(1L);
        user.setUsername("JRWTF");
        user.setFirstName("Jaime");
        user.setLastName("Jimenez");
        user.setEmail("JRWTF@wtf.com");
        user.setPassword("1234");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        album = new Album();
        album.setId(1L);

        albums = new PageImpl<>(Arrays.asList(album));

        List<Album> listaAlbums = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();
        resultado = new PagedResponse<>();
        resultado.setContent(listaAlbums);
        resultado.setTotalPages(1);
        resultado.setTotalElements(1);
        resultado.setLast(true);
        resultado.setSize(1);
    }

    //Test: Comprobar que consigue todos los albumes de un usuario
    //entrada: albumService.getUserAlbums("JRWTF",1,10)
    //salida esperada: El test se realiza con exito
    @Test
    @DisplayName("get user albums")
    void getUserAlbums_success(){
        when(userRepository.getUserByName("JRWTF")).thenReturn(user);
        Pageable pageable = PageRequest.of(1,10,Sort.Direction.DESC, CREATED_AT);

        when(albumRepository.findByUserId(1L,pageable)).thenReturn(albums);

        assertEquals(resultado,albumService.getUserAlbums("JRWTF",1,10));
    }
    //Test: Comprobar que si no existe el usuario no te imprime la lista
    //entrada: albumService.getUserAlbums("Etenciv",1,10)
    //salida esperada: El test devuelve con exito un null pointer exception.
    @Test
    @DisplayName("get user albums with diferent user that are not in the repository")
    void getUserAlbums_throwNullPointerException(){

        when(userRepository.getUserByName("JRWTF")).thenReturn(user);
        Pageable pageable = PageRequest.of(1,10,Sort.Direction.DESC, CREATED_AT);

        when(albumRepository.findByUserId(1L,pageable)).thenReturn(albums);

        assertThrows(NullPointerException.class , () -> albumService.getUserAlbums("Etenciv",1,10));
    }

}
