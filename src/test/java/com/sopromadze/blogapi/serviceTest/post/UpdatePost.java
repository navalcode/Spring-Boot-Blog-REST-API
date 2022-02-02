package com.sopromadze.blogapi.serviceTest.post;

import com.sopromadze.blogapi.controllerTest.userController.CurrentUser;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.payload.PostResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UpdatePost {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postService;

    Role role;
    User user;
    User user2;
    Post post;
    Tag tag;
    Comment comment;
    Category category;
    PostRequest newPost;

    UserPrincipal currentUser;
    UserPrincipal currentUser2;

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
        user.setPassword("Password");
        user.setRoles(List.of(role));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Username");
        user2.setFirstName("FirstName");
        user2.setLastName("LastName");
        user2.setEmail("Email@gmail.com");
        user2.setPassword("Password");
        user2.setRoles(List.of(role));
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());



        comment = new Comment();
        tag = new Tag();

        post = new Post();
        post.setId(1L);
        post.setTitle("Titulo");
        post.setBody("Body");
        post.setUser(user);
        post.setCategory(category);
        post.setComments(List.of(comment));
        post.setTags(List.of(tag));
        category = new Category();
        category.setId(1L);

        currentUser = UserPrincipal.create(user);
        currentUser2 = UserPrincipal.create(user2);



        newPost = new PostRequest();
        newPost.setTitle("Titulo");
        newPost.setCategoryId(1L);

    }

    //Test: Comprobar que se actualiza el post
    //Entrada: idPost, newPost, currentUser
    //Salida esperada: postRepository.save(post)
    @DisplayName("Update post")
    @Test
    void updatePost_success() throws Exception {
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        //when(userRepository.getUser(currentUser)).thenReturn(user);
        when(postRepository.save(post)).thenReturn(post);
        assertEquals(post, postService.updatePost(1L, newPost, currentUser));
    }

    //Test: Comprobar que se lanza la excepcion ResourceNotFoundException
    //Entrada: idPost, newPost, currentUser
    //Salida esperada: ResourceNotFoundException
    @DisplayName("Update post but throws ResourceNotFoundException")
    @Test
    void updatePost_throwsResourceNotFoundException () {
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(postRepository.save(post)).thenReturn(post);

        assertThrows(ResourceNotFoundException.class, () -> postService.updatePost(2L, newPost, currentUser));
    }

    //Test: Comprobar que se lanza la excepcion UnauthorizedException
    //Entrada: idPost, newPost, currentUser
    //Salida esperada: UnauthorizedException
    @DisplayName("Update post without authorities")
    @Test
    void updatePost_throwsUnauthorizedException () {
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
        when(postRepository.save(post)).thenReturn(post);

        assertThrows(UnauthorizedException.class,() -> postService.updatePost(1L,newPost,currentUser2));
    }
}
