package com.sopromadze.blogapi.serviceTest.user;

import com.sopromadze.blogapi.exception.AccessDeniedException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeleteUser {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /*
     * Test:check user can delete itself
     * Input:String username, UserPrincipal
     * Output:ApiResponse Success true
     * */
    @Test
    @DisplayName("Delete User works")
    void deleteUser_success(){
        //Create Roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create user
        User user = new User ();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(rolesUser);

        //Create UserPrincipal
        UserPrincipal userP= UserPrincipal.create(user);

        //Mock userRepository.findByUsername
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        //Mock userRepository.deleteById
        doNothing().when(userRepository).deleteById(Mockito.anyLong());

        assertTrue(userService.deleteUser(user.getUsername(),userP).getSuccess());

    }

    /*
     * Test:Check if admin can delete another user
     * Input:String username, AdminPrincipal
     * Output:ApiResponse True
     * */
    @Test
    @DisplayName("Delete User works for admin")
    void deleteUser_worksForAdmin(){
        //Create Roles
        List<Role> rolesAdmin= new ArrayList<Role>();
        rolesAdmin.add(new Role(RoleName.ROLE_ADMIN));
        rolesAdmin.add(new Role(RoleName.ROLE_USER));

        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create Admin
        User admin = new User ();
        admin.setId(1L);
        admin.setUsername("Authorized");
        admin.setPassword("Authorized");
        admin.setRoles(rolesAdmin);

        //Create User
        User user = new User ();
        user.setId(2L);
        user.setUsername("username");
        user.setPassword("username");
        user.setRoles(rolesUser);

        //Create UserPrincipal
        UserPrincipal adminP= UserPrincipal.create(admin);

        //Mock userRepository.findByUsername
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        //Mock userRepository.deleteById
        doNothing().when(userRepository).deleteById(Mockito.anyLong());

        assertTrue(userService.deleteUser(user.getUsername(),adminP).getSuccess());

    }

    /*
    * Test:Check AccessDeniedException is thrown when user is trying to delete another user
    * Input:String username, wrong UserPrincipal
    * Output:Throws AccessDeniedException
    * */
    @Test
    @DisplayName("Delete User throws AccesDeniedException")
    void deleteUser_fails(){
        //Create Roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create Admin
        User notAdmin = new User ();
        notAdmin.setId(1L);
        notAdmin.setUsername("not Authorized");
        notAdmin.setPassword("not Authorized");
        notAdmin.setRoles(rolesUser);

        //Create User
        User user = new User ();
        user.setId(2L);
        user.setUsername("username");
        user.setPassword("username");
        user.setRoles(rolesUser);

        //Create UserPrincipal
        UserPrincipal userP= UserPrincipal.create(notAdmin);

        //Mock userRepository.findByUsername
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        //Mock userRepository.deleteById
        doNothing().when(userRepository).deleteById(Mockito.anyLong());

        assertThrows(AccessDeniedException.class,()->userService.deleteUser(user.getUsername(),userP));

    }

    /*
     * Test:Check if ResourceNotFoundException is thrown with non-existent user
     * Input:String username,  AdminPrincipal
     * Output:Throws ResourceNotFoundException
     * */
    @Test
    @DisplayName("Delete User throws ResourceNotFoundException")
    void deleteUser_failsWhenUserEmpty(){
        //Create Roles
        List<Role> rolesAdmin= new ArrayList<Role>();
        rolesAdmin.add(new Role(RoleName.ROLE_ADMIN));
        rolesAdmin.add(new Role(RoleName.ROLE_USER));

        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create Admin
        User admin = new User ();
        admin.setId(1L);
        admin.setUsername("Authorized");
        admin.setPassword("Authorized");
        admin.setRoles(rolesAdmin);

        //Create UserPrincipal
        UserPrincipal adminP= UserPrincipal.create(admin);

        //Mock userRepository.findByUsername
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->userService.deleteUser(Mockito.anyString(),adminP).getSuccess());

    }

}
