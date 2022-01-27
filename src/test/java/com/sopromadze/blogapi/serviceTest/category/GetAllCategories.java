package com.sopromadze.blogapi.serviceTest.category;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllCategories {


    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    CategoryServiceImpl categoryService;

    PagedResponse<Category> resultadoEsperado;

    @BeforeEach
    void init() {
        Pageable pageable = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");
       Category category = new Category();
        category.setName("categoria1");
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        Page<Category> categories = new PageImpl<>(Collections.singletonList(category));
        System.out.println(categories);


        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categories);
        List<Category> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(content);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

    }


    /*
        Test:               Comprobar la paginacion con todas las categorias
        Entrada:            categoryService.getAllCategories(page(1),size(1))
        Salida esperada:    El test se realiza con exito
    */
    @Test
    @DisplayName("Get all categories")
    void getAllCategories_success()  {

        assertEquals(resultadoEsperado,categoryService.getAllCategories(1,1));
    }

    @Test
    @DisplayName("Not getting category list")
    void getAllCategories_success_fail(){
        assertNotEquals(new PagedResponse<>(),categoryService.getAllCategories(1,1));
    }




}
