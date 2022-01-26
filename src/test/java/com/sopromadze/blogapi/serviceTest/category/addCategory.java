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
    UserPrincipal userPrincipal;

    @BeforeEach
    void init() {
        User user = new User("Usuario1", "Apellido1", "Username", "usuario@gmail.com", "1234");
        user.setId(1L);

        userPrincipal = new UserPrincipal(1L, "Usuario1", "Apellido1", "Username", "usuario@gmail.com", "1234", Collections.emptyList());

        category = new Category("Categoria 1");
    }

    @DisplayName("add new category")
    @Test
    void addCategory_success() {
        ResponseEntity<Category> response = new ResponseEntity<>(category, HttpStatus.CREATED);
        assertEquals(categoryService.addCategory(category, userPrincipal), response);
    }


}
