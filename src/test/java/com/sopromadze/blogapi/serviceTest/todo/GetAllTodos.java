package com.sopromadze.blogapi.serviceTest.todo;

import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.TodoRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness =  Strictness.LENIENT)
public class GetAllTodos {

    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoServiceImpl todoService;

    Pageable pageable;
    Page<Todo> todos;
    Todo todo;
    List<Todo> content;
    List<Role> roles;
    User user;
    UserPrincipal userPrincipal;
    PagedResponse pagedResponse;

    @BeforeEach
    void init(){
        pageable = PageRequest.of(1,1);

        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_USER));

        user = new User();
        user.setId(1L);
        user.setUsername("Evil etenciv");
        user.setLastName("Rufo bruh");
        user.setEmail("Etenciv@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword("1234");
        user.setRoles(roles);

        userPrincipal = UserPrincipal.create(user);

        todo = new Todo();
        todo.setId(1L);
        todo.setUser(user);
        todo.setTitle("Todo para hacer");
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());

        todos = new PageImpl<>(Arrays.asList(todo));

        content = todos.getNumberOfElements() == 0 ? Collections.emptyList() : todos.getContent();

        pagedResponse = new PagedResponse();
        pagedResponse.setContent(content);
        pagedResponse.setLast(true);
        pagedResponse.setPage(0);
        pagedResponse.setSize(1);
        pagedResponse.setTotalElements(1);
        pagedResponse.setTotalPages(1);
    }

    //Test: Comprobar que te devuelve todos los todos
    //entrada: todoService.getAllTodos(userPrincipal,1,1)
    //salida esperada: El test devuelve el pagedResponse correspondiente.
    @DisplayName("get all todos")
    @Test
    void getAllTodos_success(){
        when(todoRepository.findByCreatedBy(Mockito.any(),Mockito.any())).thenReturn(todos);
        assertEquals(pagedResponse,todoService.getAllTodos(userPrincipal,1,1));
    }
}
