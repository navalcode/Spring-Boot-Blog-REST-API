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

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindByTags {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init() {
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

        Tag tag = new Tag();
        tag.setName("Tag1");
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());


        p.setUser(user);
        List<Tag> tgs= new ArrayList<Tag>();
        tgs.add(tag);
        p.setTags(tgs);
        List<Post> lp= new ArrayList<Post>();
        tag.setPosts(lp);

        testEntityManager.persist(p);
        testEntityManager.persist(ca);
        testEntityManager.persist(user);
        testEntityManager.persist(tag);


    }

    @Test
    @DisplayName("Find by tags")
    void findByTags_success() {

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Post> result = postRepository.findByTagsIn(tgs, pageable);

        assertTrue(result.getTotalElements()!=0);
    }
}
