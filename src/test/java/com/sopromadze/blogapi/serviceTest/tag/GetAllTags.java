package com.sopromadze.blogapi.serviceTest.tag;

import com.sopromadze.blogapi.model.Tag;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.TagRepository;
import com.sopromadze.blogapi.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.CREATED_AT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllTags {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    TagServiceImpl tagService;

    PagedResponse<Tag> resultadoEsperado;
    Page<Tag> resultado2;

    @BeforeEach
    void init() {
        Tag tag = new Tag();
        resultado2 = new PageImpl<>(Arrays.asList(tag));
        List<Tag> content = resultado2.getNumberOfElements() == 0 ? Collections.emptyList() : resultado2.getContent();

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(content);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

    }


    //Test: Comprobar que se consigue todos los tags
    //Entrada: tagService.getAllTags(1,1)
    //Salida esperada: PagedResponse<Tag>
    @DisplayName("Get all tags")
    @Test
    void getAllTags_success() {
        when(tagRepository.findAll(any(Pageable.class))).thenReturn(resultado2);
        assertEquals(resultadoEsperado, tagService.getAllTags(1, 1));
    }


    //Test: Comprobar que devuelve un pageable vacio
    //Entrada: tagService.getAllTags(1,1)
    //Salida esperada: PagedResponse<Tag> vacio
    @DisplayName("Get all tags fail")
    @Test
    void getAllTags_fail() {
        resultado2 = Page.empty();
        when(tagRepository.findAll(any(Pageable.class))).thenReturn(resultado2);
        when(modelMapper.map(any(), any())).thenReturn(resultado2);

        assertEquals(0, tagService.getAllTags(1, 10).getTotalElements());
    }
}
