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
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FindByCategoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init(){
        //Category creation
        Category ca = new Category("category");
        ca.setCreatedAt(Instant.now());
        ca.setUpdatedAt(Instant.now());

        //Post creation
        Post p= new Post();
        p.setCategory(ca);
        p.setCreatedAt(Instant.now());
        p.setUpdatedAt(Instant.now());
        p.setTitle("post");
        p.setBody("body");

        //Comments creation
        List<Comment> coments= new ArrayList<Comment>();
        p.setComments(coments);

        //User creation
        User user = new User();
        user.setEmail("user@email.com");
        user.setFirstName("FirstName");
        user.setUsername("UserName");
        user.setLastName("LastName");
        user.setPassword("password");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        p.setUser(user);
        List<Tag> tgs= new ArrayList<Tag>();
        p.setTags(tgs);
        List<Post> lp= new ArrayList<Post>();

        testEntityManager.persist(p);
        testEntityManager.persist(ca);
        testEntityManager.persist(user);
    }

    //Test: Find by category id.
    //Entrada: Long categoryId, Pageable
    //Salida esperada: Page<post from category name>
    @Test
    @DisplayName("Find by category")
    void findByCateogy_success(){

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Post> result =postRepository.findByCategoryId(1L,pageable);

        assertTrue(result.getTotalElements()!=0);
    }

    //Test: Find by non-existent category .
    //Entrada: Long categoryId, Pageable
    //Salida esperada:  page with no elements
    @Test
    @DisplayName("Find by non-existent category")
    void findByCateogy_fail(){

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Post> result =postRepository.findByCategoryId(0L,pageable);

        assertFalse(result.getTotalElements()!=0);
    }
}
