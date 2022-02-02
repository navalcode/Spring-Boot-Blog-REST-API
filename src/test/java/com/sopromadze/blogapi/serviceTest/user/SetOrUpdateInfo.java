package com.sopromadze.blogapi.serviceTest.user;

import com.sopromadze.blogapi.exception.AccessDeniedException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.Address;
import com.sopromadze.blogapi.model.user.Company;
import com.sopromadze.blogapi.model.user.Geo;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.InfoRequest;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class SetOrUpdateInfo {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private UserServiceImpl userService;

    Role role;
    User user;
    User user2;
    UserPrincipal userPrincipal;
    InfoRequest infoRequest;
    UserPrincipal userPrincipal2;
    Geo geo;
    Address address;
    Company company;
    ApiResponse apiResponse;
    UserProfile userProfile;


    @BeforeEach
    void init(){
        role = new Role();
        role.setId(1L);
        role.setName(RoleName.ROLE_USER);
        List<Role> listRole = new ArrayList<>();
        listRole.add(role);

        user = new User();
        user.setId(1L);
        user.setUpdatedAt(Instant.now());
        user.setEmail("Alejandro@gmail.com");
        user.setPassword("1234");
        user.setFirstName("Ale");
        user.setUsername("Martin");
        user.setCreatedAt(Instant.now());
        user.setRoles(listRole);

        userPrincipal = UserPrincipal.create(user);
        user2 = new User();
        user2.setId(2L);
        user2.setUpdatedAt(Instant.now());
        user2.setEmail("Alejandro@gmail.com");
        user2.setPassword("1234");
        user2.setFirstName("Ale");
        user2.setUsername("Martin");
        user2.setCreatedAt(Instant.now());
        user2.setRoles(listRole);

        userPrincipal2 = UserPrincipal.create(user2);

        infoRequest = new InfoRequest();
        infoRequest.setLat("37.3827100");
        infoRequest.setLng("-6.0025700");
        infoRequest.setStreet("Puente de triana");
        infoRequest.setSuite("Puente");
        infoRequest.setCity("Sevilla");
        infoRequest.setZipcode("41001");

        geo = new Geo();
        geo.setLat(infoRequest.getLat());
        geo.setLng(infoRequest.getLng());

        address = new Address(infoRequest.getStreet(), infoRequest.getSuite(), infoRequest.getCity(),
                infoRequest.getZipcode(), geo);

        company = new Company();

        user.setAddress(address);
        user.setCompany(company);
        user.setWebsite(infoRequest.getWebsite());
        user.setPhone(infoRequest.getPhone());

        apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update users profile", HttpStatus.FORBIDDEN);

        userProfile =  new UserProfile(user.getId(), user.getUsername(),
                user.getFirstName(), user.getLastName(), user.getCreatedAt(),
                user.getEmail(), user.getAddress(), user.getPhone(), user.getWebsite(),
                user.getCompany(), 1L);
    }
    //Test: Comprobar que te se añada o actualice la info del usuario
    //entrada: userService.setOrUpdateInfo(userPrincipal,infoRequest)
    //salida esperada: El test añade o actualiza correctamente
    @DisplayName("set or update info")
    @Test
    void setOrUpdateInfo_success(){
        when(userRepository.findByUsername(Mockito.any())).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(postRepository.countByUserId(1L)).thenReturn(1L);

        assertEquals(userProfile,userService.setOrUpdateInfo(userPrincipal,infoRequest));
    }
    //Test: Comprobar que la excepcion se lanza con exito
    //entrada: userService.setOrUpdateInfo(userPrincipal2,infoRequest)
    //salida esperada: El test lanza la excepcion correctamente
    @DisplayName("set or update info throws AccessDeniedException")
    @Test
    void setOrUpdateInfo_throwsAccessDeniedException(){
        when(userRepository.findByUsername("Martin")).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(postRepository.countByUserId(1L)).thenReturn(1L);

        assertThrows(AccessDeniedException.class,() -> userService.setOrUpdateInfo(userPrincipal2,infoRequest));
    }
}
