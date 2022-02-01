package com.sopromadze.blogapi.serviceTest.tag;

import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddTag {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    Tag tag;
    User user;
    List<Role> roles;
    UserPrincipal userPrincipal;

    @BeforeEach
    void init(){
        roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_USER));

        user = new User();
        user.setId(1L);
        user.setUsername("etenciv");
        user.setLastName("Rufo bruh");
        user.setEmail("Etenciv@hotmail.com");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setPassword("1234");
        user.setRoles(roles);

        userPrincipal = UserPrincipal.create(user);

        tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag to guapo");
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());

    }

    //Test: Comprobar que te se añade un nuevo tag
    //entrada: tagService.addTag(tag,userPrincipal)
    //salida esperada: El test añade correctamente
    @DisplayName("add tag")
    @Test
    void addTag_success(){
        when(tagRepository.save(tag)).thenReturn(tag);
        assertEquals(tag,tagService.addTag(tag,userPrincipal));
    }


}
