package com.sopromadze.blogapi.serviceTest.comment;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.service.impl.CommentServiceImpl;
import com.sopromadze.blogapi.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class getComment {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @InjectMocks
    PostServiceImpl postService;

    Long postId;
    Comment comment;
    Post post;


    @BeforeEach
    void init() {
        post = new Post();
        post.setId(1L);
        comment = new Comment();
        comment.setId(1L);
        comment.setPost(post);




    }


    // Test: Comprobar que el servicio de comment devuelve un comment existente
    // Entrada: id, postId
    // Salida esperada: Comment
    @Test
    void getComment_success() {

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        assertEquals(commentService.getComment(1L, 1L), comment);

    }

    //Test: Comprobar que el servicio comment devuelve una excepción not found
    //Entrada: id, postId igualados a 0
    //Salida esperada: ResourceNotFoundException
    @Test
    void getComment_fails() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        assertThrows(ResourceNotFoundException.class,() -> commentService.getComment(0L, 0L));
    }

    //Test: Comprobar que el servicio post devuelve una excepción not found
    //Entrada: id = 0L
    //Salida esperada: ResourceNotFoundException
    @Test
    void getComment_failsPost() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        assertThrows(ResourceNotFoundException.class,() -> postService.getPost(0L));
    }
}
