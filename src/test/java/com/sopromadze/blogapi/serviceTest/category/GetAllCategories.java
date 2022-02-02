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
import java.util.function.BooleanSupplier;

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

    Page<Category> categories;

    @BeforeEach
    void init() {
        Pageable pageable = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");
       Category category = new Category();
        category.setName("categoria1");
        category.setCreatedAt(Instant.now());
        category.setUpdatedAt(Instant.now());
        categories = new PageImpl<>(Arrays.asList(category));
    }


    /*
        Test:               Comprobar la paginacion con todas las categorias
        Entrada:            categoryService.getAllCategories(page(1),size(1))
        Salida esperada:    El test se realiza con exito
    */
    @DisplayName("Get all categories")
    @Test
    void getAllCategories_success()  {
        Pageable pageable = PageRequest.of(1, 20, Sort.Direction.DESC, "createdAt");

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categories);
        List<Category> content = categories.getNumberOfElements() == 0 ? Collections.emptyList() : categories.getContent();

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(content);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);
        assertEquals(resultadoEsperado,categoryService.getAllCategories(1,1));
    }

    /*
    Test:               Comprobar la excepcion de la paginacion con todas las categorias cuando no se añade contenido a la paginacion
    Entrada:            categoryService.getAllCategories(page(1),size(1))
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName("Get all categories fail")
    @Test
    void getAllCategories_success_fail(){

        assertThrows(NullPointerException.class ,
                ()->categoryService.getAllCategories( 1, 1));
    }

}
