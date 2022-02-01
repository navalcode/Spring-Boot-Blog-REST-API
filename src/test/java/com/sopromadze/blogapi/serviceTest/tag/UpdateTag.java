package com.sopromadze.blogapi.serviceTest.tag;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PhotoRequest;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.PhotoRepository;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class UpdateTag {


    @Mock
    private TagRepository tagRepository;


    @InjectMocks
    private TagServiceImpl tagService;

    Role role;
    User user;
    Tag tag;
    ApiResponse apiResponse;
    UserPrincipal userP;


    @BeforeEach
    void init() {
        role = new Role();
        role.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setPassword("1234");
        user.setRoles(List.of(role));
        user.setUsername("JoseDo");
        user.setFirstName("Domingo");
        user.setLastName("Perez");
        user.setEmail("josedo50@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userP= UserPrincipal.create(user);

        Post post = new Post();
        post.setId(1L);

        tag = new Tag();
        tag.setName("Tag");
        tag.setId(1L);
        tag.setPosts(List.of(post));
        tag.setCreatedAt(Instant.now());
        tag.setCreatedBy(1L);
        tag.setUpdatedAt(Instant.now());
        apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this tag");
    }

    /*
    Test:               Modificar un tag
    Entrada:            id(tag), tag(clase creada), userP(usuario actual)
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("update tag")
    @Test
    void updateTag_success(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);

        assertEquals(tag,tagService.updateTag(1L,tag,userP));
    }

    /*
    Test:               Lanzar la excepcion ResourceNotFoundException cuando se modifique un tag
    Entrada:            id(tag), tag(clase creada), userP(usuario actual)
    Salida esperada:    El test se realiza con exito cuando se lanza la excepcion
    */
    @DisplayName ("update tag Exception ResourceNotFoundException")
    @Test
    void updateTag_successWhenExceptionResourceNotFoundException(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);

        assertThrows(ResourceNotFoundException.class,() -> tagService.updateTag(2L,tag,userP));
    }

    /*
    Test:               Lanzar la excepcion UnauthorizedException cuando se modifique un tag
    Entrada:            id(tag), tag(clase creada), userP(usuario actual)
    Salida esperada:    El test se realiza con exito cuando se lanza la excepcion
    */
    @DisplayName ("update tag Exception UnauthorizedException")
    @Test
    void updateTag_successWhenExceptionUnauthorizedException(){
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);
        user.setId(2L);
        assertThrows(UnauthorizedException.class,() -> tagService.updateTag(1L,tag,UserPrincipal.create(user)));
    }

}
