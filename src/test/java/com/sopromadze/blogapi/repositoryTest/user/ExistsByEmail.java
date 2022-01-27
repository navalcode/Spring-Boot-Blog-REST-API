package com.sopromadze.blogapi.repositoryTest.user;


import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExistsByEmail {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    private User user ;

    @BeforeEach
    void init() {
        user = new User();
        user.setUsername("pedrito");
        user.setEmail("pedro784@gmail.com");
        user.setFirstName("Pedro");
        user.setLastName("Garcia Muñóz");
        user.setPassword("Contraseña.1");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        testEntityManager.persist(user);

    }
    /*
        Test:               Encontrar usuario según su email
        Entrada:            userRepository.existsByEmail(user.getEmail()))
        Salida esperada:    True, si encuentra al usuario
     */
    @Test
    void existsByEmail_success() {
        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }

    /*
        Test:               No Encontrar usuario según un email incorrecto
        Entrada:            userRepository.existsByEmail("emailIncorrecto@gmail.com"))
        Salida esperada:    True, si no encuentra al usuario
     */
    @Test
    void existsByEmail_successWhenEmailAlreadyNotExists() {
        assertFalse(userRepository.existsByEmail("emailIncorrecto@gmail.com"));

    }

}
