package com.sopromadze.blogapi.serviceTest.photo;

import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.PhotoRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeletePhoto {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoServiceImpl photoService;

    /*
    * Test:Test if the photo is deleted with user role admin
    * Input:Long photoId, UserPrincipal currentUser
    * Output:ApiResponse true
    * */
    @Test
    @DisplayName("Delete photo succes")
    void deletePhoto_success(){
        //Create Album
        Album album = new Album();
        album.setTitle("album");

        //Create Photo
        Photo photo = new Photo();
        photo.setTitle("NaturalPhoto");

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

        //Setting user to album
        album.setUser(admin);

        //Setting album to photo
        photo.setAlbum(album);

        //Create expected apiResponse
        ApiResponse apiResponse = new ApiResponse(true, "photo deleted succesfully");

        //mockear photorepo para que devuelva photo
        when(photoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(photo));
        //mockear photorepo para que elimine photorepository
        doNothing().when(photoRepository).deleteById(Mockito.anyLong());

        assertTrue(photoService.deletePhoto(Mockito.anyLong(), AdminP).getSuccess());
    }

    /*
     * Test:Test if the photo is deleted with user role user and photo is not from user
     * Input:Long photoId, UserPrincipal currentUser
     * Output: UnautorizedException containing ApiResponse false
     * */
    @Test
    @DisplayName("Delete photo fail")
    void deletePhoto_fail(){
        //Create Album
        Album album = new Album();
        album.setTitle("album");

        //Create Photo
        Photo photo = new Photo();
        photo.setTitle("NaturalPhoto");

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

        //Create UserPrincipal
        UserPrincipal UserP= UserPrincipal.create(user);

        //Setting user to album
        album.setUser(user2);

        //Setting album to photo
        photo.setAlbum(album);

        //Create expected apiResponse
        ApiResponse apiResponse = new ApiResponse(false, "You don't have permission to delete this photo");

        when(photoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(photo));
        doNothing().when(photoRepository).deleteById(Mockito.anyLong());

        assertThrows(UnauthorizedException.class, () -> photoService.deletePhoto(Mockito.anyLong(), UserP));
    }
}
