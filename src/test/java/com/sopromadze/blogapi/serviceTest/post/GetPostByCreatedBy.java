package com.sopromadze.blogapi.serviceTest.post;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetPostByCreatedBy {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostServiceImpl postService;

    User user;
    Pageable pageable;
    Post post;
    Page<Post> posts;
    List<Post> content;
    List<Role> roles;
    PagedResponse pagedResponse;

    @BeforeEach
    void init(){
        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_USER));

        post = new Post();
        post.setId(1L);
        post.setTitle("Titulo de post");


        user = new User ();
        user.setId(1L);
        user.setUsername("Fran");
        user.setLastName("Gallego");
        user.setEmail("Fran@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword("1234");
        user.setRoles(roles);
        pageable = PageRequest.of(1,1);
        posts = new PageImpl<>(Arrays.asList(post));

        content = posts.getNumberOfElements() == 0 ? Collections.emptyList() : posts.getContent();

        pagedResponse = new PagedResponse();
        pagedResponse.setContent(content);
        pagedResponse.setLast(true);
        pagedResponse.setPage(0);
        pagedResponse.setSize(1);
        pagedResponse.setTotalElements(1);
        pagedResponse.setTotalPages(1);
    }

    //Test: Comprobar que te devuelve el post creado por el usuario
    //entrada: postService.getPostsByCreatedBy("Fran",1,1)
    //salida esperada: El test devuelve el pagedResponse correspondiente.
    @DisplayName("get post by created by")
    @Test
    void getPostByCreatedBy_success(){
        when(userRepository.getUserByName("Fran")).thenReturn(user);
        when(postRepository.findByUserId(Mockito.any(),Mockito.any())).thenReturn(posts);

        assertEquals(pagedResponse,postService.getPostsByCreatedBy("Fran",1,1));

    }
    //Test: Comprobar que lanza la excepcion nullPointerException
    //entrada: postService.getPostsByCreatedBy("Hola",1,1)
    //salida esperada: El test devuelve la excepción esperada.
    @DisplayName("get post by created by but user doesn't exists")
    @Test
    void getPostByCreatedBy_fails(){
        when(userRepository.getUserByName("Fran")).thenReturn(user);
        when(postRepository.findByUserId(Mockito.any(),Mockito.any())).thenReturn(posts);

        assertThrows(NullPointerException.class,() -> postService.getPostsByCreatedBy("Hola",1,1));

    }
}
