package com.sopromadze.blogapi.repositoryTest.todo;

import com.sopromadze.blogapi.model.Todo;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.Instant;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByUserId {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init(){
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

        //Todo creation
        Todo todo= new Todo();
        todo.setTitle("Todo");
        todo.setCreatedAt(Instant.now());
        todo.setUpdatedAt(Instant.now());
        todo.setUser(user);

        testEntityManager.persist(todo);
    }

    /*
    * Test: Comprobar si FindByUserId funciona al pasarle un id existente
    * Entrada: 1L
    * Salida esperada: Optional<Post>
    * */
    @Test
    void findByUserId_success(){
        Page<Todo> result= todoRepository.findByUserId(1L, PageRequest.of(1,25));
        assertTrue(result.getTotalElements()!=0);
    }

    /*
     * Test: Comprobar que se devuelve una pagina vacia al recibir un id inexistente
     * Entrada: 0L
     * Salida esperada: Page<Post> con 0 elementos
     * */
    @Test
    void findByUserId_fails(){
        Page<Todo> result= todoRepository.findByUserId(0L, PageRequest.of(1,25));
        assertEquals(0,result.getTotalElements());
    }
}
