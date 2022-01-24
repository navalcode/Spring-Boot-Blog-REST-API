package com.sopromadze.blogapi;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VerifyDataJpaTest {

    @Autowired
    private AlbumRepository repository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testRepoNotNull() {
        assertNotNull(repository);
    }

    @Test
    void test_findAll() {

        Album album = new Album();
        album.setTitle("Título");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        testEntityManager.persist(album);

        assertNotEquals(0, repository.findAll().size());


    }


}
