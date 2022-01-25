package com.sopromadze.blogapi.repositoryTest.photo;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindByAlbumId {

    @Autowired
    private PhotoRepository repository;

    @Autowired
    private TestEntityManager entityManager;
    //Test: Comprobar que el repositorio no es nulo
    //entrada: assertNotNull(repository)
    //salida esperada: el test funciona correctamente
    @Test
    void repositoryNotNull_success(){
        assertNotNull(repository);
    }
    //Test: comprobar que se encuentra el album por su Id
    //entrada: repository.findByAlbumId(1l,pageable)
    //salida esperada: el test funciona correctamente
    @Test
    void findByAlbumId_success(){
        Album album = new Album();
        album.setTitle("Título");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        entityManager.persist(album);

        Photo photo = new Photo();
        photo.setTitle("Album to wapo");
        photo.setUrl("www.hola.com");
        photo.setThumbnailUrl("hola");
        photo.setCreatedAt(Instant.now());
        photo.setUpdatedAt(Instant.now());
        photo.setAlbum(album);

        entityManager.persist(photo);
        Pageable pageable = (Pageable) PageRequest.of(0,10);

        assertNotEquals(0,repository.findByAlbumId(1l,pageable).getTotalElements());
    }
}
