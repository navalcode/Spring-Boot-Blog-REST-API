package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
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

    private List<Role> adminRoles;
    private List<Role> userRoles;
    private User adminUser;
    private User casualUser;
    private User propertyUser;
    private UserPrincipal userPrincipalAdmin;
    private UserPrincipal userPrincipalCasual;

    @BeforeEach
    void init() {
        //Admin roles creation
        adminRoles = new ArrayList<>();
        adminRoles.add(new Role(RoleName.ROLE_ADMIN));

        //User creation
        adminUser = new User(
                "Frenando",
                "Alonso",
                "frenandoalonso20",
                "frenandoalonso20@gmail.com",
                "1234");
        adminUser.setId(1L);
        adminUser.setRoles(adminRoles);

        //Normal user roles creation
        userRoles = new ArrayList<>();
        userRoles.add(new Role(RoleName.ROLE_USER));

        //Casual User creation
        casualUser = new User(
                "Franchesco",
                "Bergolinni",
                "fiaunnnnnn20",
                "fiaunnnnn20@gmail.com",
                "1111");
        casualUser.setId(2L);
        casualUser.setRoles(userRoles);

        //albumPropertyUser creation
        propertyUser = new User(
                "Propietario",
                "Propietaries",
                "propieta2020",
                "prope20@gmail.com",
                "1234");
        propertyUser.setId(3L);
        propertyUser.setRoles(userRoles);

        //UserPrincipal creation
        userPrincipalAdmin = UserPrincipal.create(adminUser);
        userPrincipalCasual = UserPrincipal.create(casualUser);
    }

    /* Test: Comprobar que borra un album
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: un código 200, de haber borrado exitosamente el álbum */
    @DisplayName("delete album successfully")
    @Test
    void deleteAlbum_success(){

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");
        a1.setUser(propertyUser);

        Album a =new Album();
        a.setId(1L);
        a.setTitle("Coches de carreras");
        a.setUser(propertyUser);

        when(albumRepository.save(any())).thenReturn(a);
        when(albumRepository.findById(any())).thenReturn(java.util.Optional.of(a));
        when(userRepository.getUser(any())).thenReturn(adminUser);

        albumService.addAlbum(a1, userPrincipalAdmin);

        assertEquals(HttpStatus.OK, albumService.deleteAlbum(1L,userPrincipalAdmin).getStatusCode());
    }

    /* Test: Comprobar que no borra un album porque ni el usuario que quiere borrarlo es admin ni es su propietario
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: BlogapiException, formada por un código 403 y un mensaje */
    @DisplayName("deleteAlbum without permissions")
    @Test
    void deleteAlbum_userWithoutAdminRole(){

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");
        a1.setUser(propertyUser);

        Album a =new Album();
        a.setId(1L);
        a.setTitle("Coches de carreras");
        a.setUser(propertyUser);

        when(albumRepository.save(any())).thenReturn(a);
        when(albumRepository.findById(any())).thenReturn(java.util.Optional.of(a));
        when(userRepository.getUser(any())).thenReturn(casualUser);

        albumService.addAlbum(a1, userPrincipalCasual);

        assertThrows(BlogapiException.class, () -> albumService.deleteAlbum(1L,userPrincipalCasual));
    }
}