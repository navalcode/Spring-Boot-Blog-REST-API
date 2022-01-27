package com.sopromadze.blogapi.serviceTest.category;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CategoryService;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeleteCategory {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Role> roles;
    private User user;
    private UserPrincipal userPrincipal;
    private Category category;
    private ApiResponse apiResponse;


    @BeforeEach
    void init(){
        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_ADMIN));

        user = new User();
        user.setId(1L);
        user.setUsername("AleMarCue");
        user.setLastName("Martin");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setFirstName("Alejandro");
        user.setEmail("Ale@gmail.com");
        user.setRoles(roles);

        userPrincipal = UserPrincipal.create(user);

        category = new Category();
        category.setId(1L);
        category.setCreatedBy(1L);
        category.setName("Frutas");
        apiResponse = new ApiResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("You successfully deleted category");
    }

    //Test: Comprobar que borra la categoria
    //entrada: categoryService.deleteCategory(1L,userPrincipal)
    //salida esperada: El test devuelve true y se borra la categoria
    @Test
    @DisplayName("delete category")
    void deleteCategory_success(){
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));
        doNothing().when(categoryRepository).deleteById(1L);

        assertEquals(apiResponse, categoryService.deleteCategory(1L,userPrincipal));
    }
    //Test: Comprobar que si no existe la categoria lanza ResourceNotFoundException
    //entrada: categoryService.deleteCategory(0L,userPrincipal)
    //salida esperada: El test lanza la excepcion ResourceNotFoundException
    @Test
    @DisplayName("delete category but category is null")
    void deleteCategory_throwsResourceNotFoundException(){
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));

        assertThrows(ResourceNotFoundException.class,() -> categoryService.deleteCategory(0L,userPrincipal));
    }

    //Test: Comprobar que si no existe la categoria lanza UnauthorizedException
    //entrada: categoryService.deleteCategory(1L,userPrincipal)
    //salida esperada: El test lanza la excepcion UnauthorizedException
    @Test
    @DisplayName("delete category but user is unauthorized")
    void deleteCategory_throwsUnauthorizedException(){
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));
        List<Role> roles2 = new ArrayList<>();
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Guille");
        user2.setLastName("DeLa");
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());
        user2.setFirstName("Guille");
        user2.setEmail("Guille@Gmail.com");
        user2.setRoles(roles2);

        UserPrincipal userPrincipal2 = UserPrincipal.create(user2);

        assertThrows(UnauthorizedException.class,() -> categoryService.deleteCategory(1L,userPrincipal2));
    }
}
