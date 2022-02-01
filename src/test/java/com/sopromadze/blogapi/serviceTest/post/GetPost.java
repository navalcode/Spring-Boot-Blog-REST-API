package com.sopromadze.blogapi.serviceTest.post;


import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class GetPost {

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostServiceImpl postService;

    Post post;

    @BeforeEach
    void initTest() {
        post = new Post();
        post.setId(1L);
        post.setTitle("Titulo");

    }

    /*
    Test:               Obtener un post
    Entrada:            id(post)
    Salida esperada:    El test se realiza con exito cuando obtiene el post por su id
    */
    @Test
    @DisplayName ("Get post successfully")
    void getPost_success(){
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        assertEquals(post,postService.getPost(post.getId()));
    }

    /*
    Test:               Al obtener un post, salte excepcion ResourceNotFoundException
    Entrada:            id(post)
    Salida esperada:    El test se realiza con exito cuando obtiene la excepcion en la peticion
    */
    @Test
    @DisplayName ("Get post exception ResourceNotFoundException")
    void getPost_failException(){
        when(postRepository.findById(1L)).thenReturn(java.util.Optional.of(post));
        assertEquals(post,postService.getPost(post.getId()));

        assertThrows(ResourceNotFoundException.class, () -> postService.getPost(0L));

    }

}
