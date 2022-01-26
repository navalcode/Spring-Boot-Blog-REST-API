package com.sopromadze.blogapi.repositoryTest.tag;


import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.TagRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles ("test")
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FindByName {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Tag tag;

    @BeforeEach
    void InitTest(){
        tag = new Tag();
        tag.setName("nombreTag");
        tag.setCreatedAt(Instant.now());
        tag.setUpdatedAt(Instant.now());

        testEntityManager.persist(tag);

    }

    //Test:                 Comprobar se encuentra al tag por nombre
    //entrada:              tagRepository.findByName("nombreTag")
    //salida esperada:      Si el test resulta exitoso devuelve el tag con ese nombre
    @Test
    @DisplayName ("Find by nameTag")
    void findByName_success(){

        assertEquals(tag,tagRepository.findByName("nombreTag"));
    }


    //Test:                 Comprobar que  no contenga el nombre del tag
    //entrada:              tagRepository.findByName("pepe")
    //salida esperada:      Si el test resulta exitoso si la consulta devuelve un valor nulo
    @Test
    @DisplayName("find by name tag no exits")
    void findByName_fail(){
        assertNull(tagRepository.findByName("pepe"));
    }


}
