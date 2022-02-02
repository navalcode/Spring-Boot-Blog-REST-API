package com.sopromadze.blogapi.controllerTest.userController;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.TestDisableSecurityConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.AlbumService;
import com.sopromadze.blogapi.service.UserService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest (classes = TestDisableSecurityConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GetUserAlbum {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private UserService userService;



    /*
     Test:               Petición para mostrar los albums del usuario
     Entrada:            get("/api/users/{username}/albums","Pepe777")
     Salida esperada:    Test exitoso, codigo de respuesta correcto (200)
     */
    @Test
    void getUserAlbum_success() throws Exception{


        User user = new User();
        user.setId(1L);
        user.setFirstName("Pepe");
        user.setUsername("Pepe777");
        user.setLastName("Garcia");
        user.setPassword("12345");
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));
        user.setRoles(rolesUser);

        Album album = new Album();
        album.setUser(user);
        PagedResponse<Album> response = new PagedResponse<>();
        response.setContent(List.of(album));

        when(albumService.getUserAlbums(user.getUsername(),1,1)).thenReturn(response);

        mockMvc.perform(get("/api/users/{username}/albums","Pepe777")
                        .contentType("application/json")
                .param("page","1").param("size","1")
        ).andExpect(status().isOk());



    }




}
