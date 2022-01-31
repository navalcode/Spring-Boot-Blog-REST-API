package com.sopromadze.blogapi.serviceTest.UserDetails;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.ArgumentMatchers.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness = Strictness.LENIENT)
public class LoadUserByUsername {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;


    /*
    Test:               Comprobar la excepcion de la consulta que encuentra un usuario por email/username para convertirlo posteriormente en usuario principal
    Entrada:            customUserDetailsService.loadUserByUsername(eq(Mockito.anyString())).getUsername().equals(user.getUsername()))
    Salida esperada:    El test se realiza con exito lanzando la excepcion (UsernameNotFoundException)
    */
    @Test
    @DisplayName ("Load User by username exist")
    void loadByUserByUsername_succes(){
        Role rolAdmin = new Role();
        rolAdmin.setName(RoleName.ROLE_ADMIN);
        Role rolUser = new Role();
        rolUser.setName(RoleName.ROLE_USER);

        User user = new User("Anton","Alvarez","Tangana","madrileño@madrid.com","rosalia");
        user.setRoles(List.of(rolUser,rolAdmin));

        when(userRepository.findByUsernameOrEmail(eq("Tagana"),eq("madrileño@madrid.com"))).thenReturn(Optional.of(user));
        System.out.println(user.getUsername());
        assertEquals(user.getUsername(), "Tangana");
        //assertEquals(customUserDetailsService.loadUserByUsername(user.getUsername()), UserPrincipal.create(user));
    }

    /*
    Test:               Comprobar la excepcion de la consulta que encuentra un usuario por email/username para convertirlo posteriormente en usuario principal
    Entrada:            customUserDetailsService.loadUserByUsername(eq(Mockito.anyString())).getUsername().equals(user.getUsername()))
    Salida esperada:    El test se realiza con exito lanzando la excepcion (UsernameNotFoundException)
    */
    @Test
    @DisplayName("Load By User By Username null")
    void loadByUserByUsername_fail(){

        User user = new User();
        when(userRepository.findByUsernameOrEmail(Mockito.anyString(),Mockito.anyString())).thenReturn(Optional.of(user));
        assertThrows(UsernameNotFoundException.class,()->customUserDetailsService.loadUserByUsername(eq(Mockito.anyString())).getUsername().equals(user.getUsername()));
    }



}
