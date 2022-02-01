package com.sopromadze.blogapi.serviceTest.todo;


import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.TodoRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith (MockitoExtension.class)
@MockitoSettings (strictness =  Strictness.LENIENT)
public class addTodo {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    User user;
    UserPrincipal userP;
    Todo todo;

    @BeforeEach
    void initTest(){
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        user = new User();
        user.setId(1L);
        user.setRoles(rolesUser);

        todo= new Todo();

        userP = UserPrincipal.create(user);
    }

    /*
    Test:               Comprobar que se añade un usuario a un todo
    Entrada:            todoService.addTodo(todo,userP),todo
    Salida esperada:    El test se realiza con exito
    */
    @DisplayName ("Add todo successfully")
    @Test
    void addPhoto_success() {
        when(userRepository.getUser(userP)).thenReturn(user);
        todo.setUser(user);
        when(todoRepository.save(todo)).thenReturn(todo);

        assertEquals(todoService.addTodo(todo,userP),todo);
    }


    /*
    Test:               Comprobar que NO se añade un usuario a un todo
    Entrada:            todoService.addTodo(todo,userP),todo
    Salida esperada:    El test se realiza con exito comprobando que no devuelve un todo vacio o no añade al usuario
    */
    @DisplayName ("Not Add todo successfully")
    @Test
    void addPhoto_success_fail() {
        when(userRepository.getUser(userP)).thenReturn(user);
        todo.setUser(user);
        when(todoRepository.save(todo)).thenReturn(todo);

        assertNotEquals(todoService.addTodo(todo,userP),new Todo());
        User userFail = new User();
        userFail.setId(2L);
        assertNotEquals(todoService.addTodo(todo,userP).getUser(),userFail);
    }


}
