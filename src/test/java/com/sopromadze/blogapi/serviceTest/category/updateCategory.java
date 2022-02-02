package com.sopromadze.blogapi.serviceTest.category;

import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class updateCategory {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    private List<Role> adminRoles;
    private List<Role> userRoles;
    private User user;
    private User normalUser;
    private UserPrincipal userPrincipal;
    private UserPrincipal normalUserPrincipal;
    private Category catOriginal;
    private Category catCorregida;

    @BeforeEach
    void init() {
        //Admin roles creation
        adminRoles = new ArrayList<>();
        adminRoles.add(new Role(RoleName.ROLE_ADMIN));

        userRoles = new ArrayList<>();
        userRoles.add(new Role(RoleName.ROLE_USER));

        //Category creation
        catOriginal = new Category(); //id 1
        catOriginal.setName("History");
        catOriginal.setCreatedAt(Instant.now());
        catOriginal.setUpdatedAt(Instant.now());

        //Correct Category creation
        catCorregida = new Category(); //id 2
        catCorregida.setName("Historia");
        catCorregida.setCreatedAt(Instant.now());
        catCorregida.setUpdatedAt(Instant.now());

        //User creation
        user = new User(
                "Franchesco",
                "Bergolinni",
                "fiaunnnnnn20",
                "fiaunnnnn20@gmail.com",
                "1111");
        user.setId(4L);
        user.setRoles(adminRoles);

        //User creation
        normalUser = new User(
                "Franchesco",
                "Bergolinni",
                "fiaunnnnnn20",
                "fiaunnnnn20@gmail.com",
                "1111");
        normalUser.setId(5L);
        normalUser.setRoles(userRoles);

        userPrincipal = UserPrincipal.create(user);
        normalUserPrincipal = UserPrincipal.create(normalUser);

        catOriginal.setCreatedBy(3L);
        catCorregida.setCreatedBy(3L);
    }

    /*
    Test: Comprobar que actualiza una categoria
    Entrada: Long id, Category newCategory, UserPrincipal currentUser
    salida esperada: un código 200, de haber actualizado bien la categoria
    */
    @DisplayName("update category successfully")
    @Test
    void updateCategory_success() {
        when(categoryRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(catOriginal));
        when(categoryRepository.save(any())).thenReturn(catCorregida);

        assertEquals(HttpStatus.OK,categoryService.updateCategory(1L, catCorregida, userPrincipal).getStatusCode());

    }

    /*
    Test: Comprobar que no actualiza una categoria porque el usuario no tiene permisos de Admin
    Entrada: Long id, Category newCategory, UserPrincipal currentUser
    salida esperada: UnauthorizedException
    */
    @DisplayName("update category without permissions")
    @Test
    void updateCategory_withoutAdminPermissions() {
        when(categoryRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(catOriginal));

        assertThrows(UnauthorizedException.class, () -> categoryService.updateCategory(1L, catCorregida, normalUserPrincipal));

    }

}
