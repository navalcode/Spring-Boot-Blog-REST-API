package com.sopromadze.blogapi.repositoryTest.user;

import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByUsername {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init() {

        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("Username");
        user.setLastName("Lastname");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);
    }

    //Test: Find by username
    //Entrada: String username
    //Salida esperada: Optional<User from username>
    @Test
    @DisplayName("Find by username")
    void findByUsername_success() {
        Optional<User> result = userRepository.findByUsername("Username");

        assertTrue(result.isPresent());
    }

    //Test: Find by non-existent username
    //Entrada: String username
    //Salida esperada: Optional<User from username>
    @Test
    @DisplayName("Find by non-exists username")
    void findByUsername_fail() {
        Optional<User> result = userRepository.findByUsername("UsernameFalso");

        assertFalse(result.isPresent());
    }
}
