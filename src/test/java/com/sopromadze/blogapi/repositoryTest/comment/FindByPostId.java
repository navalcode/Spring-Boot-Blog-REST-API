package com.sopromadze.blogapi.repositoryTest.comment;

import com.sopromadze.blogapi.model.*;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.CommentRepository;
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

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByPostId {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init() {


        //User creation
        User u1 = new User();
        u1.setEmail("randomUser1@gmail.com");
        u1.setFirstName("random");
        u1.setUsername("randomUser1");
        u1.setLastName("user");
        u1.setPassword("password");
        u1.setCreatedAt(Instant.now());
        u1.setUpdatedAt(Instant.now());

        User u2 = new User();
        u2.setEmail("juan32@gmail.com");
        u2.setFirstName("juan");
        u2.setUsername("juanlu22");
        u2.setLastName("luis guerra");
        u2.setPassword("contra1234");
        u2.setCreatedAt(Instant.now());
        u2.setUpdatedAt(Instant.now());

        //Comment creation
        Comment c1 = new Comment();
        c1.setName("The white horse");
        c1.setEmail("juan32@gmail.com");
        c1.setBody("The white horse that Santiago had, make him invincible");
        c1.setCreatedAt(Instant.now());
        c1.setUpdatedAt(Instant.now());

        //Category creation
        Category cat = new Category();
        cat.setName("History");
        cat.setCreatedAt(Instant.now());
        cat.setUpdatedAt(Instant.now());

        //Tag creation
        Tag t = new Tag();
        t.setName("Saint");
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(Instant.now());

        //Post creation
        Post p1 =new Post();
        p1.setTitle("Saint Santiago Stories");
        p1.setBody("Anyone knows facts or stories about the Saint Santiago and his white horse?");
        p1.setUser(u2);
        p1.setCategory(cat);
        p1.getComments().add(c1);
        p1.getTags().add(t);
        p1.setCreatedAt(Instant.now());
        p1.setUpdatedAt(Instant.now());

        c1.setPost(p1);
        cat.getPosts().add(p1);
        t.getPosts().add(p1);

        testEntityManager.persist(u1);
        testEntityManager.persist(u2);
        testEntityManager.persist(c1);
        testEntityManager.persist(cat);
        testEntityManager.persist(t);
        testEntityManager.persist(p1);

    }

    @Test
    @DisplayName("Find by post id success")
    void findByPostId_success() {

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Comment> result = commentRepository.findByPostId(1L, pageable);

        assertTrue(result.getTotalElements()!=0);

    }

    @Test
    @DisplayName("Find by post id false")
    void findByPostId_fail() {

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Comment> result = commentRepository.findByPostId(25L, pageable);

        assertFalse(result.getTotalElements()!=0);

    }
}