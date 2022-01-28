package com.sopromadze.blogapi.repositoryTest.user;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Optional;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByUsernameOrEmail {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    //Test: Find by username with no email.
    //Entrada: String Username, empty email
    //Salida esperada: Optional<User>
    @Test
    @DisplayName("Find only by Username")
    void findByUsername_success(){
        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        Optional<User> opUser= Optional.of(user);

        Optional<User> result =userRepository.findByUsernameOrEmail("UserName","");

        assertTrue(result.isPresent());
    }

    //Test: Find by email with no userName.
    //Entrada: String email, empty userName
    //Salida esperada: Optional<User>
    @Test
    @DisplayName("Find only by email")
    void findByEmail_success(){
        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        Optional<User> opUser= Optional.of(user);

        Optional<User> result =userRepository.findByUsernameOrEmail("","user@email.com");

        assertTrue(result.isPresent());
    }

    //Test: Find by email and userName.
    //Entrada: String email, String userName
    //Salida esperada: Optional<User>
    @Test
    @DisplayName("Find with matching userName and Email")
    void findByUsernameOrEmail_success(){
        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        Optional<User> opUser= Optional.of(user);

        Optional<User> result =userRepository.findByUsernameOrEmail("UserName","user@email.com");

        assertTrue(result.isPresent());
    }

    //Test: Find by wrong email and wrong userName.
    //Entrada: String wrongEmail, String wrongUserName
    //Salida esperada: Optional<User>
    @Test
    @DisplayName("Find by wrong userName or email")
    void findByUsernameOrEmail_fails(){
        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        testEntityManager.persist(user);

        Optional<User> opUser= Optional.of(user);

        Optional<User> result =userRepository.findByUsernameOrEmail("","");

        assertFalse(result.isPresent());
    }
}
