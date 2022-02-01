package com.sopromadze.blogapi.controllerTest.commentController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CommentService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UpdateComment {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    /*
     Test:               Petición para modificar un comment
     Entrada:            put("/api/posts/{postId}/comments/{id}",1L,1L
     Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
     */
    @DisplayName("update comment return 200")
    @Test
    @WithMockUser(authorities = {"ROLE_USER","ROLE_ADMIN"})
    void updateComment_return200() throws Exception{

        Post post = new Post();
        post.setId(1L);

        CommentRequest content = new CommentRequest();
        content.setBody("Esto es el cuerpo de un comment requerido");
        User user = new User();
        user.setId(1L);
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        Comment comment = new Comment();

        when(commentService.updateComment(1L, 1L, content, userP)).thenReturn(comment);


        mockMvc.perform(put("/api/posts/{postId}/comments/{id}",1L,1L)
                        .contentType("application/json")
                        .param("size", "1").param("page","1")
                        .content((objectMapper.writeValueAsString(content)))
                ).andExpect(status().isOk());

    }

    /*
    Test:               Petición para modificar un comment sin contenido
    Entrada:            put("/api/posts/{postId}/comments/{id}",1L,1L)
    Salida esperada:    Test exitoso, codigo de respuesta correcto (400)
    */
    @DisplayName("update comment return 400")
    @Test
    @WithMockUser(authorities = {"ROLE_USER","ROLE_ADMIN"})
    void updateComment_return400() throws Exception{

        Post post = new Post();
        post.setId(1L);

        CommentRequest content = new CommentRequest();
        content.setBody("Esto es el cuerpo de un comment requerido");
        User user = new User();
        user.setId(1L);
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        Comment comment = new Comment();

        when(commentService.updateComment(1L, 1L, content, userP)).thenReturn(comment);


        mockMvc.perform(put("/api/posts/{postId}/comments/{id}",1L,1L)
                .contentType("application/json")
                .param("size", "1").param("page","1")
        ).andExpect(status().isBadRequest());

    }


    /*
     Test:               Petición para modificar un comment
     Entrada:            put("/api/posts/{postId}/comments/{id}",1L,1L
     Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
     */
    @DisplayName("update comment return 403")
    @Test
    void updateComment_return403() throws Exception{

        Post post = new Post();
        post.setId(1L);

        CommentRequest content = new CommentRequest();
        content.setBody("Esto es el cuerpo de un comment requerido");
        User user = new User();
        user.setId(1L);
        List<Role> rolesUser = new ArrayList<Role>();
        user.setRoles(rolesUser);
        UserPrincipal userP = UserPrincipal.create(user);
        Comment comment = new Comment();

        when(commentService.updateComment(1L, 1L, content, userP)).thenReturn(comment);


        mockMvc.perform(put("/api/posts/{postId}/comments/{id}",1L,1L)
                .contentType("application/json")
                .param("size", "1").param("page","1")
                .content((objectMapper.writeValueAsString(content)))
        ).andExpect(status().isForbidden());

    }

}
