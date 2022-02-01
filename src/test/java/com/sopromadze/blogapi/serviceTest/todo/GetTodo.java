package com.sopromadze.blogapi.serviceTest.todo;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.TodoRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TodoServiceImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness =  Strictness.LENIENT)
public class GetTodo {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    /*
    * Test:Check if getTodo success
    * Input: Long todoId, UserPrincipal currentUser
    * Output:Appropriate todo
    * */
    @Test
    @DisplayName("Get Todo success")
    void getTodo_success(){
        //Create roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create User
        User user = new User();
        user.setId(1L);
        user.setRoles(rolesUser);

        //Create Todo
        Todo todo= new Todo();
        todo.setTitle("title");
        todo.setUser(user);

        UserPrincipal up = UserPrincipal.create(user);

        when(userRepository.getUser(Mockito.any())).thenReturn(user);
        when(todoRepository.findById(Mockito.any())).thenReturn(Optional.of(todo));

        assertTrue(todoService.getTodo(1L,up).getTitle().equals(todo.getTitle()));

    }

    /*
     * Test:Check if getTodo fails when UserPrincipal do not match todo User
     * Input: Long todoId, Wrong UserPrincipal currentUser
     * Output:Throws UnauthorizedException
     * */
    @Test
    @DisplayName("Get Todo fails with wrong user")
    void getTodo_failsWhenUserIdDoNotMatchWithPostCreatedBy(){
        //Create roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create User
        User user = new User();
        user.setId(1L);
        user.setRoles(rolesUser);

        //Create User2
        User user2 = new User();
        user2.setId(2L);
        user2.setRoles(rolesUser);

        //Create Todo
        Todo todo= new Todo();
        todo.setTitle("title");
        todo.setUser(user2);

        UserPrincipal up = UserPrincipal.create(user);

        when(userRepository.getUser(Mockito.any())).thenReturn(user);
        when(todoRepository.findById(Mockito.any())).thenReturn(Optional.of(todo));

        assertThrows(UnauthorizedException.class,()-> todoService.getTodo(1L,up));
    }

    /*
     * Test:Check if getTodo throws ResourceNotFoundException when id is wrong
     * Input: wrong Long todoId, UserPrincipal currentUser
     * Output:ResourceNotFoundException
     * */
    @Test
    @DisplayName("Get Todo NotFound")
    void getTodo_failsWhenTodoNotFound(){
        //Create roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create User
        User user = new User();
        user.setId(1L);
        user.setRoles(rolesUser);

        UserPrincipal up = UserPrincipal.create(user);

        when(todoRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()-> todoService.getTodo(1L,up));
    }
}
