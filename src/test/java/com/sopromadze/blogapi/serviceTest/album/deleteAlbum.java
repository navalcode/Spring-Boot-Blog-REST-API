package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
            casualUser.setId(1L);
            casualUser.setRoles(userRoles);

        //UserPrincipal creation
        userPrincipalAdmin = UserPrincipal.create(adminUser);
        userPrincipalCasual = UserPrincipal.create(casualUser);
    }

    /* Test: Comprobar que borra un album
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: un código 200, de haber borrado exitosamente el álbum */
    @Test
    void deleteAlbum_success(){

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");
        a1.setUser(adminUser);

        Album a =new Album();
        a.setId(1L);
        a.setTitle("Coches de carreras");
        a.setUser(adminUser);

        when(albumRepository.save(any())).thenReturn(a);
        when(albumRepository.findById(any())).thenReturn(java.util.Optional.of(a));
        when(userRepository.getUser(any())).thenReturn(adminUser);

        albumService.addAlbum(a1, userPrincipalAdmin);

        assertEquals(HttpStatus.OK, albumService.deleteAlbum(1L,userPrincipalAdmin).getStatusCode());
    }

    /* Test: Comprobar que borra un album
    entrada: albumService.deleteAlbum(albumid, UserPrincipal)
    salida esperada: un código 200, de haber borrado exitosamente el álbum */
    @Test
    void deleteAlbum_userWithoutAdminRole(){

        //AlbumRequest creation
        AlbumRequest a1 =new AlbumRequest();
        a1.setId(1L);
        a1.setTitle("Coches de carreras");
        a1.setUser(casualUser);

        Album a =new Album();
        a.setId(1L);
        a.setTitle("Coches de carreras");
        a.setUser(casualUser);

        when(albumRepository.save(any())).thenReturn(a);
        when(albumRepository.findById(any())).thenReturn(java.util.Optional.of(a));
        when(userRepository.getUser(any())).thenReturn(casualUser);

        albumService.addAlbum(a1, userPrincipalCasual);

        //Devuelve un 200
        assertEquals(HttpStatus.FORBIDDEN, albumService.deleteAlbum(1L,userPrincipalCasual).getStatusCode());
    }
}
