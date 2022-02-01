package com.sopromadze.blogapi.serviceTest.comment;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddComment {
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    //CommentRequest Creation
    private CommentRequest cr;

    //Post Creation
    private Post post;

    //UserPrincipal Creation
    private UserPrincipal userPrincipal;

    //User Creation
    private User user;

    //Comment creation
    private Comment comment;


        @BeforeEach
        void init(){
            //Initialize
            cr = new CommentRequest();
            post= new Post();
            userPrincipal = new UserPrincipal(1L,"firstName","lastName","username","email@email.com","password",null);
            user = new User("firstName","lastName","username","email@email.com","password");
            comment = new Comment(cr.getBody());

            //Adding some extra data
            cr.setBody("Un mensaje de más de 10 letras");
            post.setTitle("Test post");
            comment.setUser(user);
            comment.setPost(post);
            comment.setName("firstName");
            comment.setEmail("email@email.com");
        }

    /*
    * Test: To check if addComment works with all the params
    * Input:CommentRequest, Long postId, UserPrincipal current
    * Output:Expected comment
    * */
    @Test
    @DisplayName("AddComment Succes")
    void addComment_success(){

        when(postRepository.findById(Mockito.any())).thenReturn(Optional.of(post));
        when(userRepository.getUser(Mockito.any())).thenReturn(user);
        when(commentRepository.save(Mockito.any())).thenReturn(comment);

        assertAll(
                ()->assertTrue(commentService.addComment(cr,Mockito.any(),userPrincipal).getName().equals(comment.getName())),
                ()->assertTrue(commentService.addComment(cr,Mockito.any(),userPrincipal).getEmail().equals(comment.getEmail())),
                ()->assertTrue(commentService.addComment(cr,Mockito.any(),userPrincipal).getUser().getUsername().equals(userPrincipal.getUsername())),
                ()->assertTrue(commentService.addComment(cr,Mockito.any(),userPrincipal).getPost().getTitle().equals(post.getTitle()))
        );
    }

    /*
     * Test: To check if addComment will return propper Exception when no post is found
     * Input:CommentRequest, Wrong Long postId, UserPrincipal current
     * Output:Resource Not Found Exception
     * */
    @Test
    @DisplayName("AddComment fails wrong post id")
    void addComment_fails(){
        when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->commentService.addComment(cr,Mockito.any(),userPrincipal));
    }

}
