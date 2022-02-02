package com.sopromadze.blogapi.serviceTest.user;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetUserProfile {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private UserServiceImpl userService;


    Post post;
    List<Role> roles;
    User user;
    UserPrincipal userPrincipal;
    UserProfile userProfile;

    @BeforeEach
    void init(){

        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_USER));

        user = new User();
        user.setId(1L);
        user.setUsername("Good etenciv");
        user.setFirstName("Vicente");
        user.setLastName("Rufo bruh");
        user.setEmail("Etenciv@hotmail.com");
        user.setAddress(new Address());
        user.setPhone("123456789");
        user.setWebsite("hola.com");
        user.setCompany(new Company());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword("1234");
        user.setRoles(roles);

        userPrincipal = UserPrincipal.create(user);

        post = new Post();
        post.setId(1L);
        post.setUser(user);
        post.setTitle("Post nuevo");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        userProfile = new UserProfile();
        userProfile.setId(1L);
        userProfile.setUsername("Good etenciv");
        userProfile.setFirstName("Vicente");
        userProfile.setLastName("Rufo bruh");
        userProfile.setJoinedAt(user.getCreatedAt());
        userProfile.setEmail("Etenciv@hotmail.com");
        userProfile.setAddress(new Address());
        userProfile.setPhone("123456789");
        userProfile.setWebsite("hola.com");
        userProfile.setCompany(new Company());
        userProfile.setPostCount(1L);
    }

    //Test: Comprobar que te devuelve el userProfile
    //entrada: userService.getUserProfile("Good etenciv")
    //salida esperada: El test devuelve el UserProfile correspondiente.
    @DisplayName("get user profile")
    @Test
    void getUserProfile_success(){
        when(userRepository.getUserByName("Good etenciv")).thenReturn(user);
        when(postRepository.countByUserId(1L)).thenReturn(1L);

        assertEquals(userProfile,userService.getUserProfile("Good etenciv"));
    }


}
