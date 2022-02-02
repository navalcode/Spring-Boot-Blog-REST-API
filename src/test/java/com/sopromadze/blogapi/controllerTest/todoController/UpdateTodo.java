package com.sopromadze.blogapi.controllerTest.todoController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.service.impl.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class UpdateTodo {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoServiceImpl todoService;

    private Todo todo;

    @BeforeEach
    void init(){
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("todo");
    }
    /*Test: Check if update todo returns 200 when updated
     * Input:Long TodoId, RequestBody newTodo, UserPrincipal Current
     * Output: ResponseEntity<newTodo>
     * */
    @Test
    @DisplayName("Tag controller update todo success")
    @WithUserDetails("user")
    void updateTodo_success() throws Exception{
        String todoJson = objectMapper.writeValueAsString(todo);

        when(todoService.updateTodo(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenReturn(todo);

        MvcResult mvcResult = mockMvc.perform(put("/api/todos/{id}",1L)
                        .contentType("application/json")
                        .content(todoJson))
                .andExpect(status().isOk()).andReturn();

        String actualInt = mvcResult.getResponse().getContentAsString();

        Todo actual= objectMapper.readValue(actualInt,Todo.class);
        Todo expected = objectMapper.readValue(todoJson,Todo.class);

        assertTrue(actual.getTitle().equals(expected.getTitle()));
        assertTrue(actual.getId().equals(expected.getId()));
    }

    /*Test:Check if update Todo throws 401 when user is not logged in
     * Input:Long TodoId, new Todo
     * Output:ResponseEntity<Todo> with status 401 unauthorized
     * */
    @Test
    @DisplayName("Controller update todo unauthorized")
    void updateTodo_unauthorized () throws Exception{
        String todoJson = objectMapper.writeValueAsString(todo);

        when(todoService.updateTodo(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenReturn(todo);

        MvcResult mvcResult = mockMvc.perform(put("/api/todos/{id}",1L)
                        .contentType("application/json")
                        .content(todoJson))
                .andExpect(status().isUnauthorized()).andReturn();

    }

    /*Test:Check if update Todo throws 404 when tag is not found
     * Input:TodoId, TodoRequest, UserPrincipal
     * Output:ResponseEntity<Todo> with status 404 not found
     * */
    @Test
    @DisplayName("Controller update todo not found")
    @WithUserDetails("user")
    void updateTodo_notFound () throws Exception{
        String todoJson = objectMapper.writeValueAsString(todo);

        when(todoService.updateTodo(Mockito.anyLong(),Mockito.any(),Mockito.any())).thenThrow(new ResourceNotFoundException("Todo","id",1L));

        MvcResult mvcResult = mockMvc.perform(put("/api/todos/{id}",1L)
                        .contentType("application/json")
                        .content(todoJson))
                .andExpect(status().isNotFound()).andReturn();
    }

    /*Test:Check if update todo throws 400 when the request is invalid
     * Input:TodoId, wrong Todo
     * Output:ResponseEntity<Todo> with status 400 bad request
     * */
    @Test
    @DisplayName("Controller update todo bad request")
    @WithUserDetails("user")
    void updateTodo_badRequest() throws Exception{
        String todoJson = "Not a real todo json";

        MvcResult mvcResult = mockMvc.perform(put("/api/todos/{id}",1L)
                        .contentType("application/json")
                        .content(todoJson))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
