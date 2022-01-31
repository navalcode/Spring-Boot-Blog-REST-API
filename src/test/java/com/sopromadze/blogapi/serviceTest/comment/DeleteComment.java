package com.sopromadze.blogapi.serviceTest.comment;

import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
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
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeleteComment {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    Post post;
    Comment comment;
    User user;
    User user2;
    Role role;
    UserPrincipal userPrincipal;
    UserPrincipal userPrincipal2;

    @BeforeEach
    void init(){
        post = new Post();
        post.setId(1L);
        post.setTitle("Post bien guapo");
        post.setCreatedBy(1L);


        role = new Role();
        role.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setUsername("inma.dvgs");
        user.setFirstName("Inmaculada");
        user.setLastName("Dominguez vargas");
        user.setEmail("Inmadvgs@hotmail.com");
        user.setPassword("1234");
        user.setRoles(List.of(role));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Evil inma.dvgs");
        user2.setFirstName("Inmaculada");
        user2.setLastName("Dominguez vargas");
        user2.setEmail("Inmadvgs@hotmail.com");
        user2.setPassword("1234");
        user2.setRoles(List.of(role));
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());



        comment = new Comment();
        comment.setId(1L);
        comment.setCreatedBy(1L);
        comment.setPost(post);
        comment.setUser(user);
        comment.setName("Comentario del post bien guapo");

        userPrincipal= UserPrincipal.create(user);
        userPrincipal2 = UserPrincipal.create(user2);
    }

    //Test: Comprobar que se borra el comentario
    //Entrada: 1L,1L,userPrincipal
    //Salida esperada: El test se realiza con exito y se borra el comentario
    @DisplayName("delete comment")
    @Test
    void deleteComment_success(){
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));

        doNothing().when(commentRepository).deleteById(isA(Long.class));

        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"You successfully deleted comment");

        assertEquals(apiResponse,commentService.deleteComment(1L,1L,userPrincipal));
    }
    //Test: Comprobar que se lanza la excepcion ResourceNotFoundException
    //Entrada: 1L,1L,userPrincipal
    //Salida esperada: El test se realiza con exito y se lanza ResourceNotFoundException
    @DisplayName("delete comment but post id doesn't match")
    @Test
    void deleteComment_throwsResourceNotFoundException(){
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));
        doNothing().when(commentRepository).deleteById(isA(Long.class));

        assertThrows(ResourceNotFoundException.class,() -> commentService.deleteComment(2L,1L,userPrincipal));
    }

    //Test: Comprobar que se lanza la excepcion ResourceNotFoundException
    //Entrada: 1L,1L,userPrincipal
    //Salida esperada: El test se realiza con exito y se lanza ResourceNotFoundException
    @DisplayName("delete comment but user doesn't match")
    @Test
    void deleteComment_throwsBlogapiException(){
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));
        doNothing().when(commentRepository).deleteById(isA(Long.class));

        assertThrows(BlogapiException.class,() -> commentService.deleteComment(1L,1L,userPrincipal2));
    }

}
