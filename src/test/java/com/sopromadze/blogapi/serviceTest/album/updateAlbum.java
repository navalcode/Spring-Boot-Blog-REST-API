package com.sopromadze.blogapi.serviceTest.album;


import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.request.AlbumRequest;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.PhotoRepository;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class updateAlbum {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    Album album;
    User user;
    UserPrincipal currentUser;
    AlbumRequest newAlbum;
    AlbumResponse albumResponse;
    Role role;


    @BeforeEach
    void init() {
        role = new Role();
        role.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setUsername("Username");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("Email@gmail.com");
        user.setPassword("password");
        user.setRoles(List.of(role));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        currentUser = UserPrincipal.create(user);



        album = new Album();
        album.setId(1L);
        album.setTitle("Titulo");
        album.setUser(user);




        newAlbum = new AlbumRequest();
        newAlbum.setTitle("Titulo2");
        newAlbum.setId(1L);
        albumResponse = new AlbumResponse();
        albumResponse.setId(1L);


    }
    /*@DisplayName("Update album")
    @Test
    void updateAlbum_success() {
        ResponseEntity<AlbumResponse> response = new ResponseEntity<>(albumResponse,HttpStatus.OK);
        when(albumRepository.findById(1L)).thenReturn(Optional.ofNullable(album));
        when(userRepository.getUser(currentUser)).thenReturn(user);
        newAlbum.setTitle("Titulo3");
        when(albumRepository.save(album)).thenReturn(album);
        modelMapper.map(newAlbum, albumResponse);


        assertEquals(response, albumService.updateAlbum(1L,newAlbum, currentUser));
    }*/
}
