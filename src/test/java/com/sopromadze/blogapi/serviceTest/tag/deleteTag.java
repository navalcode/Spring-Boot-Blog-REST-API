package com.sopromadze.blogapi.serviceTest.tag;

import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class deleteTag {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    /*Test: Check if the Tag is deleted with Admin role user
    * Input:Long TagId, UserPrincipal currentUser
    * Output:ApiResponse true
    * */
    @Test
    @DisplayName("Delete Tag success")
    void deleteTag_success(){
        //Create Roles
        List<Role> rolesAdmin = new ArrayList<Role>();
        rolesAdmin.add(new Role(RoleName.ROLE_ADMIN));
        rolesAdmin.add(new Role(RoleName.ROLE_USER));

        //Create UserAdmin
        User admin = new User ();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRoles(rolesAdmin);

        //Create UserPrincipal
        UserPrincipal AdminP= UserPrincipal.create(admin);

        //Create tag
        Tag tag = new Tag();
        tag.setName("tag");

        //Set tag id to admin id
        tag.setCreatedBy(1L);

        //Create expected apiResponse
        ApiResponse apiResponse = new ApiResponse(true, "You successfully deleted tag");

        //Mock tagRepository to return tag
        when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tag));
        //Mock tagRepository to return delete tag
        doNothing().when(tagRepository).deleteById(Mockito.anyLong());

        assertTrue(tagService.deleteTag(Mockito.anyLong(), AdminP).getSuccess());

    }

    /*Test: Check if deleteTag throws exception when unauthorized user tries to delete his tag
     * Input:Long TagId, wrong UserPrincipal currentUser
     * Output:Throws UnauthorizedException.class
     * */
    @Test
    @DisplayName("Delete Tag fails")
    void deleteTag_fails(){
        //Create Roles
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        //Create User
        User user = new User ();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(rolesUser);

        //Create another User
        User user2 = new User ();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("user2");
        user2.setRoles(rolesUser);

        //Create UserPrincipal whith user 1L
        UserPrincipal UserP= UserPrincipal.create(user);

        //Create tag
        Tag tag = new Tag();
        tag.setName("tag");

        //Set tag id to user2 id
        tag.setCreatedBy(2L);

        //Mock tagRepository to return tag
        when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tag));
        //Mock tagRepository to return delete tag
        doNothing().when(tagRepository).deleteById(Mockito.anyLong());

        assertThrows(UnauthorizedException.class, () -> tagService.deleteTag(Mockito.anyLong(), UserP));

    }
}
