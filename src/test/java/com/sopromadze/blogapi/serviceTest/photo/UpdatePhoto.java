package com.sopromadze.blogapi.serviceTest.photo;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.PhotoRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UpdatePhoto {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private PhotoServiceImpl photoService;

    Role role;
    User user;
    User user2;
    Album album;
    Photo photo;
    PhotoRequest photoRequest;
    PhotoResponse photoResponse;
    UserPrincipal userPrincipal;
    UserPrincipal userPrincipal2;

    @BeforeEach
    void init(){
        role = new Role();
        role.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setUsername("Guille");
        user.setFirstName("Guillermo");
        user.setLastName("De la Cruz");
        user.setEmail("Guille@Hola.com");
        user.setPassword("1234");
        user.setRoles(List.of(role));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Guille");
        user2.setFirstName("Guillermo");
        user2.setLastName("De la Cruz");
        user2.setEmail("Guille@Hola.com");
        user2.setPassword("1234");
        user2.setRoles(List.of(role));
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());

        userPrincipal = UserPrincipal.create(user);
        userPrincipal2 = UserPrincipal.create(user2);

        album = new Album();
        album.setId(1L);
        album.setTitle("Titulo del album");
        album.setUser(user);

        photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Foto del album");
        photo.setThumbnailUrl("https://holaquetal.com");
        photo.setUrl("holaquetal.com");
        photo.setAlbum(album);

        photoRequest = new PhotoRequest();
        photoRequest.setTitle("Foto del album");
        photoRequest.setAlbumId(1L);
        photoRequest.setUrl("holaquetal.com");
        photoRequest.setThumbnailUrl("https://holaquetal.com");

        photoResponse = new PhotoResponse(1L,"Foto del album","holaquetal.com","https://holaquetal.com",1L);
    }

    //Test: comprobar que se actualiza la photo correctamente
    //entrada: photoService.updatePhoto(1L,photoRequest,userPrincipal)
    //salida esperada: el test funciona correctamente
    @DisplayName("update photo")
    @Test
    void updatePhoto_success(){
        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(album));
        when(photoRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(photo));
        when(photoRepository.save(photo)).thenReturn(photo);

        assertEquals(photoResponse,photoService.updatePhoto(1L,photoRequest,userPrincipal));
    }

    //Test: comprobar que se lanza la excepcion ResourceNotFoundException
    //entrada: photoService.updatePhoto(2L,photoRequest,userPrincipal)
    //salida esperada: el test funciona correctamente
    @DisplayName("update photo but throws ResourceNotFoundException")
    @Test
    void updatePhoto_throwsResourceNotFoundException(){
        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(album));
        when(photoRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(photo));
        when(photoRepository.save(photo)).thenReturn(photo);

        assertThrows(ResourceNotFoundException.class,() -> photoService.updatePhoto(2L,photoRequest,userPrincipal));
    }
    //Test: comprobar que se lanza la excepcion UnauthorizedException
    //entrada: photoService.updatePhoto(1L,photoRequest,userPrincipal2)
    //salida esperada: el test funciona correctamente
    @DisplayName("update photo but throws UnauthorizedException")
    @Test
    void updatePhoto_throwsUnauthorizedException(){
        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(album));
        when(photoRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(photo));
        when(photoRepository.save(photo)).thenReturn(photo);

        assertThrows(UnauthorizedException.class,() -> photoService.updatePhoto(1L,photoRequest,userPrincipal2));
    }



}
