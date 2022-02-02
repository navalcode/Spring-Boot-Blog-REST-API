package com.sopromadze.blogapi.serviceTest.post;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.payload.PostResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddPost {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostServiceImpl postService;

    List<Role> roles;
    Post post;
    User user;
    Tag tag;
    UserPrincipal userPrincipal;
    List<Tag> tags;
    Category category;
    PostRequest postRequest;
    PostResponse postResponse;

    @BeforeEach
    void init(){
        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_USER));

        category = new Category();
        category.setId(1L);
        category.setName("Category");

        post = new Post();
        post.setId(1L);
        post.setTitle("Tituloooo");
        post.setBody("Por favor tengo que rellenar hasta 50 caracteres para que pasa la valicacion y no se que escribir");
        post.setCategory(category);


        user = new User();
        user.setId(1L);
        user.setUsername("Fran");
        user.setLastName("Gallego");
        user.setEmail("Fran@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword("1234");
        user.setRoles(roles);

        userPrincipal = UserPrincipal.create(user);

        tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag numero 1");

        postRequest = new PostRequest();
        postRequest.setTitle("Tituloooo");
        postRequest.setBody("Por favor tengo que rellenar hasta 50 caracteres para que pasa la valicacion y no se que escribir");
        postRequest.setTags(List.of(tag.getName()));
        postRequest.setCategoryId(1L);

        postResponse = new PostResponse();
        postResponse.setTitle(post.getTitle());
        postResponse.setBody(post.getBody());
        postResponse.setCategory("Category");
        postResponse.setTags(List.of());

        tags = new ArrayList<>(postRequest.getTags().size());
    }
    //Test: Comprobar que te añade el post creado por el usuario
    //entrada: postService.addPost(postRequest,userPrincipal)
    //salida esperada: El test devuelve el postResponse correspondiente.
    @DisplayName("add post")
    @Test
    void addPost_success(){
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(category));
        when(tagRepository.findByName(Mockito.any())).thenReturn(tag);
        when(tagRepository.save(Mockito.any())).thenReturn(tag);
        when(postRepository.save(Mockito.any())).thenReturn(post);

        assertEquals(postResponse,postService.addPost(postRequest,userPrincipal));
    }
    //Test: Comprobar que te añade el post creado por el usuario
    //entrada: postService.addPost(postRequest,userPrincipal)
    //salida esperada: El test devuelve el postResponse correspondiente.
    @DisplayName("add post")
    @Test
    void addPost_throwsResourceNotFoundException(){
        postRequest.setCategoryId(2L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(category));
        when(tagRepository.findByName(Mockito.any())).thenReturn(tag);
        when(tagRepository.save(Mockito.any())).thenReturn(tag);
        when(postRepository.save(Mockito.any())).thenReturn(post);

        assertThrows(ResourceNotFoundException.class,() -> postService.addPost(postRequest,userPrincipal));
    }
}
