package com.sopromadze.blogapi.serviceTest.user;


import com.sopromadze.blogapi.exception.AppException;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GiveAdmin {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;


    User user;
    User user2;
    User user3;
    Role roleAdmin;
    Role roleUser;

    @BeforeEach
    void init() {
        roleAdmin = new Role();
        roleAdmin.setName(RoleName.ROLE_ADMIN);
        roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setUsername("Username");
        user.setFirstName("FirstName");
        user.setRoles(List.of(roleUser, roleAdmin));


        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Username2");
        user2.setRoles(List.of(roleUser));

        user3 = new User();
        user3.setId(3L);
        user3.setUsername("Username3");
        user3.setRoles(List.of(roleAdmin));

    }

    //Test: Comprueba que se añaden los roles al usuario
    //Entrada: Username
    //Salida esperada: ApiResponse(Boolean.TRUE)
    @DisplayName("Give roles to user")
    @Test
    void giveAdmin_success() {
        when(userRepository.getUserByName(user.getUsername())).thenReturn(user);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(java.util.Optional.ofNullable(user.getRoles().get(1)));
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(java.util.Optional.ofNullable(user.getRoles().get(0)));
        when(userRepository.save(user)).thenReturn(user);

        assertTrue(userService.giveAdmin(user.getUsername()).getSuccess());
    }

    //Test: Comprueba que lanza la excepcion AppException al intentar dar rol de admin sin permisos
    //Entrada: Username
    //Salida esperada: AppException
    @DisplayName("Fail to give roles with exception AppException")
    @Test
    void giveAdmin_AppException() throws AppException {
        when(userRepository.getUserByName(user2.getUsername())).thenReturn(user2);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(java.util.Optional.ofNullable(user2.getRoles().get(0)));
        when(userRepository.save(user2)).thenReturn(user2);

        assertThrows(AppException.class, () -> userService.giveAdmin(user2.getUsername()));
    }


    //Test: Comprueba que lanza la excepcion AppException al intentar dar rol de user sin permisos
    //Entrada: Username
    //Salida esperada: AppException
    @DisplayName("Fail to give roles with exception AppException")
    @Test
    void giveAdmin_AppExceptionUser() throws AppException {
        when(userRepository.getUserByName(user3.getUsername())).thenReturn(user3);
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(java.util.Optional.ofNullable(user3.getRoles().get(0)));
        when(userRepository.save(user3)).thenReturn(user3);

        assertThrows(AppException.class, () -> userService.giveAdmin(user3.getUsername()));
    }
}
