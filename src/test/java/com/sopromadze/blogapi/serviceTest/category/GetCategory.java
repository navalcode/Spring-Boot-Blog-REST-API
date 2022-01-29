package com.sopromadze.blogapi.serviceTest.category;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.repository.CategoryRepository;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetCategory {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    /*
     * Test: To Check CategoryService.getCategory returns ResponseEntity<Category>
     * Input: Any Long contained in the repository
     * Output: ResponseEntity<Category>
     * */
    @Test
    @DisplayName("Get Category returns album properly")
    void getCategory_success (){
        Category c = new Category();
        c.setName("nombre");
        c.setCreatedAt(Instant.now());
        Instant createdAt = c.getCreatedAt();

        ResponseEntity<Category> response = new ResponseEntity<>(c, HttpStatus.OK);

        when(categoryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(c));
        assertEquals(categoryService.getCategory(Mockito.anyLong()),response);
        assertAll(
                ()->assertTrue(categoryService.getCategory(Mockito.anyLong()).getBody().getCreatedAt().equals(createdAt)),
                ()->assertTrue(categoryService.getCategory(Mockito.anyLong()).getBody().getName().equals("nombre"))
        );
    }

    /*
    * Test: To check categoryService.getCategory returns exception not found
    * Input: A not contained id value
    * Output:Resource Not Found Exception
    * */
    @DisplayName("GetCategory throws exception")
    @Test
    void getCategory_fails(){
        Category c = new Category();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(c));
        assertThrows(ResourceNotFoundException.class,()->categoryService.getCategory(0L));

    }

}
