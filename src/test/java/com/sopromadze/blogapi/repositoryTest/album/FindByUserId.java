package com.sopromadze.blogapi.repositoryTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.AlbumRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindByUserId {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void init() {
        //User creation
        User u = new User();
        u.setEmail("user1@gmail.com");
        u.setFirstName("FirstName1");
        u.setUsername("Username1");
        u.setLastName("Lastname1");
        u.setPassword("password");
        u.setCreatedAt(Instant.now());
        u.setUpdatedAt(Instant.now());

        //Album creation
        Album a = new Album();
        a.setTitle("Title1");
        a.setUser(u);
        a.setCreatedAt(Instant.now());
        a.setUpdatedAt(Instant.now());

        //Photo creation
        Photo p1 = new Photo();
        p1.setTitle("Title1");
        p1.setUrl("Url1");
        p1.setThumbnailUrl("ThumbnailUrl1");
        p1.setAlbum(a);
        p1.setCreatedAt(Instant.now());
        p1.setUpdatedAt(Instant.now());

        List<Photo> photos = new ArrayList<Photo>();


        a.setPhoto(photos);

        testEntityManager.persist(u);
        testEntityManager.persist(a);
        testEntityManager.persist(p1);

    }

    //Test: Find by user id.
    //Entrada: Long userId, Pageable
    //Salida espera: Page<album from user id>
    @Test
    @DisplayName("Find by user id")
    void findByCreatedBy_success() {

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Album> result = albumRepository.findByUserId(1L, pageable);

        assertTrue(result.getTotalElements()!=0);

    }

    //Test: Find by non-existent user
    //Entrada: Long userId, Pageable
    //Salida esperada: Page with no elements
    @Test
    @DisplayName("Find by non-existent user id")
    void findByCreatedBy_fail() {

        Pageable pageable = PageRequest.of(1, 25, Sort.Direction.DESC, CREATED_AT);

        Page<Album> result = albumRepository.findByUserId(0L, pageable);

        assertFalse(result.getTotalElements()!=0);

    }
}
