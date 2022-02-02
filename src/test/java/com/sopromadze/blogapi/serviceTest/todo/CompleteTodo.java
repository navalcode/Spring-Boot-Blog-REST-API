package com.sopromadze.blogapi.serviceTest.todo;

import com.sopromadze.blogapi.exception.UnauthorizedException;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CompleteTodo {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    User user;
    User user2;
    Todo todo;
    UserPrincipal currentUser;
    UserPrincipal currentUser2;
    Role role;

    @BeforeEach
    void init() {
        role = new Role();
        role.setName(RoleName.ROLE_USER);
        user = new User();
        user.setId(1L);
        user.setUsername("Username");
        user.setFirstName("FirstName");
        user.setRoles(List.of(role));

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("Username");
        user2.setRoles(List.of(role));

        currentUser = UserPrincipal.create(user);
        currentUser2 = UserPrincipal.create(user2);

        todo = new Todo();
        todo.setId(1L);
        todo.setUser(user);
    }

    //Test: Comprobar que se actualiza el todo a completado
    //Entrada: idTodo, currentUser
    //Salida esperada: todoRepository.save(todo)
    @DisplayName("Complete todo")
    @Test
    void completeTodo_success() {
        when(todoRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(todo));
        when(userRepository.getUser(currentUser)).thenReturn(user);
        assertEquals(todo.getUser().getId(), user.getId());
        when(todoRepository.save(todo)).thenReturn(todo);
        assertEquals(todoService.completeTodo(1L, currentUser), todo);
    }

    //Test: Comprobar que se lanza la excepcion UnauthorizedException
    //Entrada: idTodo, currentUser
    //Salida esperada: UnauthorizedException
    @DisplayName("Complete todo without authorities")
    @Test
    void completeTodo_withoutAuthorities() throws Exception {
        when(todoRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(todo));
        when(userRepository.getUser(currentUser2)).thenReturn(user2);
        when(todoRepository.save(todo)).thenReturn(todo);
        assertThrows(UnauthorizedException.class, () -> todoService.completeTodo(1L, currentUser2));
    }

}
