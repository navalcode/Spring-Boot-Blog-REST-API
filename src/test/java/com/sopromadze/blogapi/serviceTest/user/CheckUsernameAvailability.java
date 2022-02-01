package com.sopromadze.blogapi.serviceTest.user;

import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CheckUsernameAvailability {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /*
    * Test:Check Username Availability returns UserIdentityAvailability(true) when user is available
    * Input: non-existent String username
    * Output:UserIdentityAvailability(true)
    * */
    @Test
    @DisplayName("Check Username availability returns true")
    void checkUsernameAvailability_succes(){
        String username= "username";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        assertTrue(userService.checkUsernameAvailability(username).getAvailable());
    }

    /*
     * Test:Check Username Availability returns UserIdentityAvailability(false) when user isn't available
     * Input: existent String username
     * Output:UserIdentityAvailability(false)
     * */
    @Test
    @DisplayName("Check Username availability returns false")
    void checkUsernameAvailability_fails(){
        String username= "username";
        when(userRepository.existsByUsername(username)).thenReturn(true);
        assertFalse(userService.checkUsernameAvailability(username).getAvailable());
    }
}
