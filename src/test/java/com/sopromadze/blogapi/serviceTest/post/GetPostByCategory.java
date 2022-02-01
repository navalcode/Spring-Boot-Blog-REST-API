package com.sopromadze.blogapi.serviceTest.post;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.PostRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class GetPostByCategory {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PostServiceImpl postService;

    Pageable pageable;
    Post post;
    Category category;
    Page<Post> posts;
    List<Post> content;
    PagedResponse pagedResponse;

    @BeforeEach
    void init(){
        category = new Category();
        category.setId(1L);
        category.setName("Categoria");

        post = new Post();
        post.setId(1L);
        post.setTitle("Post");
        category.setPosts(List.of(post));


        pageable = PageRequest.of(1,1);
        posts = new PageImpl<>(List.of(post));


        pagedResponse = new PagedResponse();
        pagedResponse.setContent(posts.getContent());
        pagedResponse.setLast(true);
        pagedResponse.setPage(0);
        pagedResponse.setSize(1);
        pagedResponse.setTotalElements(1);
        pagedResponse.setTotalPages(1);
    }

    /*
    Test:               Obtener los posts de una categoria
    Entrada:            id(category)
    Salida esperada:    El test se realiza con exito cuando obtienen los posts por su id de categoria
    */
    @Test
    @DisplayName ("Get posts by category successfully")
    void getPostsByCategory_success(){
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));
        when(postRepository.findByCategoryId(Mockito.any(),Mockito.any())).thenReturn(posts);

        assertEquals(pagedResponse,postService.getPostsByCategory(1L,1,1));
    }


    /*
    Test:               Al obtener los posts por su categoria, salte excepcion ResourceNotFoundException
    Entrada:            id(category)
    Salida esperada:    El test se realiza con exito cuando obtiene la excepcion en la peticion
    */
    @Test
    @DisplayName ("Get posts by category exception ResourceNotFoundException")
    void getPostsByCategory_failException(){
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByCategory(0L,1,1));
    }

}
