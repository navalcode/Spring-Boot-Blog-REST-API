package com.sopromadze.blogapi.repositoryTest.user;

import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExistsByUsername {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    //Test: comprobar que encuentra al usuario por el nombre
    //entrada: userRepository.existsByUsername(user.getUsername())
    //salida esperada: el test devuelve true
    @Test
    @DisplayName("exists by username true")
    void existsByUsername_success(){
        User user = new User();
        user.setUsername("KramusLiteral");
        user.setFirstName("Kramus");
        user.setLastName("Literal");
        user.setEmail("KramusLiteral@Buah.com");
        user.setPassword("BuahLiteral123");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        assertTrue(userRepository.existsByUsername(user.getUsername()));
    }
    //Test: comprobar que no encuentra el usuario por un nombre que no exista
    //entrada: userRepository.existsByUsername("BlackUlf66")
    //salida esperada: el test devuelve false
    @Test
    @DisplayName("exists by username false")
    void existsByUsername_false(){
        User user = new User();
        user.setUsername("KramusLiteral");
        user.setFirstName("Kramus");
        user.setLastName("Literal");
        user.setEmail("KramusLiteral@Buah.com");
        user.setPassword("BuahLiteral123");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        assertFalse(userRepository.existsByUsername("BlackUlf66"));
    }
}
