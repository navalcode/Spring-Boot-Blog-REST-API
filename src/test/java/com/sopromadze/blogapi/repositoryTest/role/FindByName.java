package com.sopromadze.blogapi.repositoryTest.role;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FindByName {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    //Test: comprobar que encuentra el rol por nombre
    //entrada: roleRepository.findByName(RoleName.ROLE_USER).get()
    //salida esperada: el test funciona correctamente
    @Test
    @DisplayName("find by name")
    void findByName_success(){

        Role role = new Role();
        role.setName(RoleName.ROLE_USER);

        testEntityManager.persist(role);

        assertEquals(role,roleRepository.findByName(RoleName.ROLE_USER).get());
    }
    //Test: comprobar que lanza la excepcion de los optional cuando estan vacios
    //entrada: roleRepository.findByName(RoleName.ROLE_USER).get()
    //salida esperada: el test funciona correctamente lanza NoSuchElementException
    @Test
    @DisplayName("find by name with 0 elements")
    void findByName_fail(){
        assertThrows(NoSuchElementException.class, () -> roleRepository.findByName(RoleName.ROLE_USER).get());
    }
}
