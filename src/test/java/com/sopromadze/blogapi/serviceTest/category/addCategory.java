package com.sopromadze.blogapi.serviceTest.category;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class addCategory {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    CategoryServiceImpl categoryService;

    Category category;
    Category categoryEmpty;
    UserPrincipal userPrincipal;
    User user;


    @BeforeEach
    void init() {
        user = new User("Usuario1", "Apellido1", "Username", "usuario@gmail.com", "1234");

        user.setId(1L);

        userPrincipal = new UserPrincipal(1L, "Usuario1", "Apellido1", "Username", "usuario@gmail.com", "1234", Collections.emptyList());

        category = new Category("Categoria 1");

    }
    
    //Test: Comprobar que se añade una nueva categoria
    //Entrada: category, user
    //Salida esperada: ResponseEntity<>(category, created)

    @DisplayName("add new category")
    @Test
    void addCategory_success() {
        ResponseEntity<Category> response = new ResponseEntity<>(category, HttpStatus.CREATED);
        when(userRepository.getUserByName(userPrincipal.getUsername())).thenReturn(user);
        when(categoryRepository.save(category)).thenReturn(category);
        assertEquals(categoryService.addCategory(category, userPrincipal), response);
    }

    //Test: Comprobar que no se agrega una nueva categoría
    //Entrada: category, user
    //Salida esperada: ResponseEntity<>(category, created)
    @DisplayName("Fail to add category")
    @Test
    void addCategory_fail() {
        ResponseEntity<Category> response = new ResponseEntity<>(category, HttpStatus.CREATED);
        when(categoryRepository.save(categoryEmpty)).thenReturn(categoryEmpty);
        assertNotEquals(categoryService.addCategory(categoryEmpty, userPrincipal), response);
    }



}
