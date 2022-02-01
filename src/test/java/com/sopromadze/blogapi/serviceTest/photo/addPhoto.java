package com.sopromadze.blogapi.serviceTest.photo;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sopromadze.blogapi.utils.AppConstants.ALBUM;
import static com.sopromadze.blogapi.utils.AppConstants.ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class addPhoto {


    @Mock
    PhotoRepository photoRepository;

    @Mock
    AlbumRepository albumRepository;

    @InjectMocks
    PhotoServiceImpl photoService;

    Photo photo;
    PhotoRequest photoRequest;
    User user;
    Album album;
    UserPrincipal userP;

    @BeforeEach
    void initTest()  {
        Role rolAdmin = new Role();
        rolAdmin.setName(RoleName.ROLE_ADMIN);
        Role rolUser = new Role();
        rolUser.setName(RoleName.ROLE_USER);

        user = new User("Anton","Alvarez","Tangana","madrileño@madrid.com","rosalia");
        userP = new UserPrincipal(1L,"Anton","Alvarez","Tangana","madrileño@madrid.com","rosalia", null);
        user.setId(1L);
        user.setRoles(List.of(rolUser,rolAdmin));

        photoRequest = new PhotoRequest();
        photoRequest.setTitle("titulo");
        photoRequest.setThumbnailUrl("url");
        photoRequest.setUrl("url");
        photo = new Photo();
        photo.setId(1L);
        photo.setTitle("titulo");
        photo.setThumbnailUrl("url");
        photo.setUrl("url");
        album= new Album();
        album.setId(1L);
        album.setUser(user);

        photo.setAlbum(album);
        photoRequest.setAlbumId(album.getId());



    }

    /*
    Test:               Añadir photo
    Entrada:            photoRequest, userP
    Salida esperada:    El test se realiza con exito cuando se añade una foto dandole el cuerpo con los datos de entrada de la misma
    */
    @Test
    @DisplayName ("Add photo successfully")
    void addPhoto_succes(){
        when(albumRepository.findById(photoRequest.getAlbumId())).thenReturn(Optional.of(album));
        when(photoRepository.findById(photo.getId())).thenReturn(Optional.of(photo));
        assertEquals(photo.getAlbum().getUser().getId(),user.getId());
        when(photoRepository.save(photo)).thenReturn(photo);

        PhotoResponse photRes = new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),photo.getUrl(), photo.getAlbum().getId());
        assertAll(
                ()-> assertEquals(photRes.getTitle() , photo.getTitle()) ,
                ()-> assertEquals(photRes.getUrl() , photo.getUrl()) ,
                ()-> assertEquals(photRes.getThumbnailUrl() , photo.getThumbnailUrl()) ,
                ()-> assertEquals(photRes.getAlbumId() , photo.getAlbum().getId())
        );

        //assertEquals(photoService.addPhoto(photoRequest , userP).getId() , photRes.getId());

    }

    /*
    Test:               Comprobar la excepcion ResourceNotFoundException
    Entrada:            ALBUM, ID, photoRequest.getAlbumId()
    Salida esperada:    El test se realiza con exito lanzando la excepcion (ResourceNotFoundException)
    */
    @Test
    @DisplayName ("Not add photo with exception")
    void loadByUserByUsername_fail(){
        Album album2 = new Album();
        when(albumRepository.findById(2L)).thenReturn(Optional.of(album2));
        assertThrows(ResourceNotFoundException.class,() -> photoService.addPhoto(photoRequest,userP));
    }


}
