package com.sopromadze.blogapi.repositoryTest.post;

import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.PostRepository;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CountByCreatedBy {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init() {

        //Instant date creation
        //Instant creationDate = Instant.parse("2011-02-10T10:14:35.00Z");
        //Instant editDate = Instant.parse("2021-06-10T11:53:22.00Z");

        //User creation
        User user = new User();
        user.setEmail("Manolin11@gmail.com");
        user.setFirstName("Manolin");
        user.setUsername("manolin11");
        user.setLastName("Jimenez");
        user.setPassword("12345");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        //User without posts creation
        User user2 = new User();
        user2.setEmail("sinposts@gmail.com");
        user2.setFirstName("Sin");
        user2.setUsername("sinpost20");
        user2.setLastName("Post");
        user2.setPassword("12345");
        user2.setCreatedAt(Instant.now());
        user2.setUpdatedAt(Instant.now());


        //Category creation
        Category cat = new Category();
        cat.setName("History");
        cat.setCreatedAt(Instant.now());
        cat.setUpdatedAt(Instant.now());

        //Post Creation
        Post p1 =new Post();
        p1.setTitle("Saint Santiago Stories");
        p1.setBody("Anyone knows facts or stories about the Saint Santiago and his white horse?");
        p1.setUser(user);
        p1.setCategory(cat);
        p1.setCreatedAt(Instant.now());
        p1.setUpdatedAt(Instant.now());

        Post p2 =new Post();
        p2.setTitle("El tio la vara");
        p2.setBody("La figura del 'Tio la vara' que conocemos hoy, viene realmente de un antiguo personaje llamado 'El vareador', que era la persona que sacudia los árboles de aceitunas con un palo para recogerlas");
        p2.setUser(user);
        p2.setCategory(cat);
        p2.setCreatedAt(Instant.now());
        p2.setUpdatedAt(Instant.now());

        Post p3 =new Post();
        p3.setTitle("El camino de Santiago");
        p3.setBody("Al principio, el camino de Santiago no era más que un pequeño camino que unía los pueblos de Santiago con la capital, pero con el tiempo más y más gente fue a verlo y se ha transformado en un viaje de peregrinación");
        p3.setUser(user);
        p3.setCategory(cat);
        p3.setCreatedAt(Instant.now());
        p3.setUpdatedAt(Instant.now());

        cat.getPosts().add(p1);
        cat.getPosts().add(p2);
        cat.getPosts().add(p3);

        user.setPosts(new ArrayList<>());
        user.getPosts().add(p1);
        user.getPosts().add(p2);
        user.getPosts().add(p3);

        testEntityManager.persist(user);
        testEntityManager.persist(user2);
        testEntityManager.persist(cat);

        testEntityManager.persist(p1);
        testEntityManager.persist(p2);
        testEntityManager.persist(p3);
    }

    /*
        Test: Devuelve el numero de posts de un usuario
        Entrada: id del usuario
        Salida: 3L
     */
    @Test
    @DisplayName("Number of userPost = 3")
    void countByUserId_success(){ //Le hemos cambiado el nombre al método base porque no cogía el método correcto inferido
        assertEquals(3L, postRepository.countByUserId(1L));
    }

    /*
        Test: Devuelve un 0 porque ese usuario no ha hecho posts
        Entrada: id de usuario
        Salida: 0L
     */
    @Test
    @DisplayName("Number of userPost = 0")
    void countByUserId_userWithoutPosts(){
        assertEquals(0L, postRepository.countByUserId(2L));
    }


}
