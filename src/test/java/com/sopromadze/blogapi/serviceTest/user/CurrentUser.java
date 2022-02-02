package com.sopromadze.blogapi.serviceTest.user;


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

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class CurrentUser {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserPrincipal userP;

    @BeforeEach
    void initTest() {

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        User user = new User();
        user.setId(1L);
        user.setPassword("1234");
        user.setRoles(List.of(role));
        user.setUsername("JoseDo");
        user.setFirstName("Domingo");
        user.setLastName("Perez");
        user.setEmail("josedo50@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userP= UserPrincipal.create(user);
    }

    /*
    Test:               Convierte un UserPrincipal en un UserSummary
    Entrada:            UserP(UserPrincipal)
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("Current user successfully")
    @Test
    void userCurrent_success(){
        UserSummary userSummary = new UserSummary(userP.getId(), userP.getUsername(), userP.getFirstName(),
                userP.getLastName());

        assertEquals(userSummary,userService.getCurrentUser(userP));
    }

    /*
    Test:               Compara con un id distinto al que se le pasa por parametro de entrada
                        sino es asi no hacer la conversion de UserPrincipal a UserSummary
    Entrada:            UserP(UserPrincipal)
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("Current user fail ")
    @Test
    void userCurrent_fail(){

        UserSummary userSummary = new UserSummary(3L, userP.getUsername(), userP.getFirstName(),
                userP.getLastName());

        assertNotEquals(userSummary,userService.getCurrentUser(userP));
    }

}
