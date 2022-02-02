package com.sopromadze.blogapi.serviceTest.user;

import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.UserSummary;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class UpdateUser {

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    UserPrincipal userP;
    User user;
    Role roleUser, roleAdmin;



    @BeforeEach
    void initTest() {

        roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        roleAdmin = new Role();
        roleAdmin.setName(RoleName.ROLE_ADMIN);
        user = new User();
        user.setId(1L);
        user.setPassword("1234");
        user.setRoles(List.of(roleUser));
        user.setUsername("JoseDo");
        user.setFirstName("Domingo");
        user.setLastName("Perez");
        user.setEmail("josedo50@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userP = UserPrincipal.create(user);

    }

    /*
    Test:               Modifica un usuario siendo el mismo que el UserPrincipal
    Entrada:            UserP(UserPrincipal)
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("Update user successfully (same userPrincipal and role admin)")
    @Test
    void updateUser_success(){
        when(userRepository.getUserByName("JoseDo")).thenReturn(user);
        assertTrue(user.getId().equals(userP.getId()) || user.getRoles().equals(roleAdmin.getName()));
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user,userService.updateUser(user,user.getUsername(),userP));
    }

    /*
    Test:               Salta excepcion UnauthorizedException cuando un usuario no es el mimo que
                        el UserPrincipal o tiene un rol de admin
    Entrada:            user,user.getUsername(),userP
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("Update user Exception UnauthorizedException (Not same userPrincipal and role admin)")
    @Test
    void updateUser_successWhenUserRoleAndIdNotEquals(){
        when(userRepository.getUserByName("JoseDo")).thenReturn(user);
        user.setId(3L);
        user.setRoles(List.of(roleUser));
        assertFalse(user.getId().equals(userP.getId()) || user.getRoles().equals(roleAdmin.getName()));
        when(userRepository.save(user)).thenReturn(user);
        assertThrows(UnauthorizedException.class,() -> userService.updateUser(user,user.getUsername(),userP));
    }


}