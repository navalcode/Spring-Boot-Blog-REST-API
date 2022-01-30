package com.sopromadze.blogapi.repositoryTest.post;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.PostRepository;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles ("test")
@AutoConfigureTestDatabase (replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByUserId {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;

    @BeforeEach
    void initTest(){
        Category c = new Category("category");
        c.setCreatedAt(Instant.now());
        c.setUpdatedAt(Instant.now());

        //Post
        Post p= new Post();
        p.setCategory(c);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());
        p.setTitle("post");
        p.setBody("body");

        p.setComments(new ArrayList<>());

        //User creation
        user = new User();

        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        p.setUser(user);
        List<Tag> tgs= new ArrayList<>();
        p.setTags(tgs);

        testEntityManager.persist(p);
        testEntityManager.persist(c);
        testEntityManager.persist(user);
    }


    /*
        Test:               Encontrar paginacion de Pots a través del id de usuario
        Entrada:            Long UserId, Pageable
        Salida esperada:    Resultado exitoso si encuentra al User paginado por id
     */
    @Test
    @DisplayName ("find Post pageable by user id")
    void findByUserId_success(){

        Page<Post> result =postRepository.findByUserId(user.getId(),Pageable.ofSize(2));

        assertEquals(1,user.getId(),"El id del usuario debe ser 1");
        assertTrue(result.getTotalElements()!=0,"El número de Pots nunca debe ser 0");
    }

    /*
        Test:               Encontrar paginacion de Pots a través del id de usuario
        Entrada:            Long UserId, Pageable
        Salida esperada:    Resultado exitoso si no encuentra al User paginado por id
     */
    @Test
    @DisplayName ("find Post pageable by user id")
    void findByUserId_successWhenUserNull(){

        Page<Post> result =postRepository.findByUserId(null,Pageable.unpaged());

        //assertEquals(0,result,"El usuario no debe existir");

        assertFalse(result.getTotalElements()!=0,"El número de Pots debe ser 0");
    }
}
