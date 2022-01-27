package com.sopromadze.blogapi.controllerTest.album;

import com.sopromadze.blogapi.controller.AlbumController;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AlbumController.class)
public class deleteAlbum {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlbumService albumService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void deleteAlbum_return204() throws Exception {
        User user = new User();
        user.setRoles(new ArrayList<Role>((Collection<? extends Role>) new Role(RoleName.ROLE_ADMIN)));

        mockMvc.perform(delete("/api/albums/{id}",1L)
                .contentType("application/json"))
                .andExpect(status().isCreated());
    }






}
