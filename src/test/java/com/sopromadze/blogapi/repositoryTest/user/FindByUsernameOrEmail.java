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

import javax.persistence.EntityManager;
import java.time.Instant;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByUsernameOrEmail {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init (){

    }

    @Test
    void findByUsernameOrEmail_successWithUsername (){
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
    }
}
