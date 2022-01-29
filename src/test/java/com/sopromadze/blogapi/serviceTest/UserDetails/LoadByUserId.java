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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoadByUserId {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;

    /*
     *Test: To check if loadUserById returns propper UserDetails.
     *Input: Existing Long id in userRepository
     *Output: UserDetails
     */
    @Test
    @DisplayName("loadUserById works when User exists")
    void loadByUserId_succes(){
        //Create Role
        Role r1 = new Role();
        r1.setName(RoleName.ROLE_ADMIN);
        Role r2 = new Role();
        r2.setName(RoleName.ROLE_USER);
        //Create user
        User user = new User("firstName","lastName","username","email@email.com","password");
        user.setRoles(List.of(r1,r2));

         //Mock user repository
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertTrue(customUserDetailsService.loadUserById(Mockito.anyLong()).getUsername().equals(user.getUsername()));
    }

    /*
    * Test:To check if an exception is throw when the user id is wrong
    * Input:Wrong Long id
    * Output:UserNameNotFoundException
    * */
    @Test
    @DisplayName("loadByUserId fails and throws Exception")
    void loadByUserId_fails(){

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,()-> customUserDetailsService.loadUserById(Mockito.anyLong()));
    }
}