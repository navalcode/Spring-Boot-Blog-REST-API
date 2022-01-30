package com.sopromadze.blogapi.controllerTest.albumController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
public class AddAlbum {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlbumServiceImpl albumService;

    /*
    * Test:Check if addAlbum returns 201 when created
    * Input: AlbumRequest, UserPrincipal
    * Output:ResponseEntity Created with proper album body
    * */
    @Test
    @DisplayName("Controller Add Album success")
    @WithUserDetails("UserP")
    void addAlbum_success() throws Exception {
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        User user = new User();
        user.setFirstName("user");
        user.setLastName("user");
        user.setEmail("user@email.com");
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(rolesUser);
        UserPrincipal userPrincipal= UserPrincipal.create(user);

        Album album = new Album();
        album.setUser(user);

        when(albumService.addAlbum(Mockito.any(), Mockito.any())).thenReturn(album);

        mockMvc.perform(post("/api/albums")
                .contentType("aplication/json")
                .content(objectMapper.writeValueAsString(album)))
                .andExpect(status().isCreated());

    }
}
