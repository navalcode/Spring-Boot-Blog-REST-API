package com.sopromadze.blogapi.controllerTest.categoryController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.configurationSecurity.RestResponsePage;
import com.sopromadze.blogapi.configurationSecurity.SpringSecurityTestConfig;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.service.impl.CategoryServiceImpl;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {SpringSecurityTestConfig.class})
@AutoConfigureMockMvc
class GetAllCategories {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryServiceImpl categoryService;

    PagedResponse<Category> resultadoCompleto;
    PagedResponse<Category> resultadoVacio;
    Page resultCompleteElement;
    Page resultEmptyElement;

    @BeforeEach
    void initTest() {

        Category cat1 = new Category();
        cat1.setName("Comida");
        cat1.setCreatedAt(Instant.now());
        cat1.setUpdatedAt(Instant.now());

        Category cat2 = new Category();
        cat2.setName("Tecnologia");
        cat2.setCreatedAt(Instant.now());
        cat2.setUpdatedAt(Instant.now());

        Category cat3 = new Category();
        cat3.setName("Arte");
        cat3.setCreatedAt(Instant.now());
        cat3.setUpdatedAt(Instant.now());

        List<Category> listaLlena = new ArrayList<Category>();
        List<Category> listaVacia = Collections.EMPTY_LIST;

        listaLlena.add(cat1); listaLlena.add(cat2); listaLlena.add(cat3);

        resultadoCompleto = new PagedResponse<>();
        resultadoCompleto.setContent(listaLlena);
        resultadoCompleto.setTotalPages(1);
        resultadoCompleto.setTotalElements(1);
        resultadoCompleto.setLast(true);
        resultadoCompleto.setSize(1);

        resultadoVacio = new PagedResponse<>();
        resultadoVacio.setContent(listaVacia);
        resultadoVacio.setTotalPages(1);
        resultadoVacio.setTotalElements(0);
        resultadoVacio.setLast(true);
        resultadoVacio.setSize(0);

        resultEmptyElement = new RestResponsePage(List.of(resultadoVacio));
        resultCompleteElement = new RestResponsePage(List.of(resultadoCompleto));
    }

    /*
    Test: Ver todas las categorias
    Entrada: GET("/api/categories")
    Salida esperada: Lista de todas las categorias
    */
    @Test
    void getAllCategories_success() throws Exception {

        PagedResponse<Category> expected = resultadoCompleto;

        when(categoryService.getAllCategories(1, 1)).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/api/categories")
                .contentType("application/json")
                .param("size", "1").param("page", "1")
                .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(resultCompleteElement)))
        )
                .andExpect(status().isOk()).andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expected);
        assertEquals(expectedJson, actual);

    }

    /*
    Test: Mandar una pagina sin sin elementos ni tamaño
    Entrada: get("/api/categories")
    Salida esperada: Pagina vacia sin elementos ni tamaño
    */
    @Test
    void getAllCategories_emptyCategoryPage() throws Exception {

        PagedResponse<Category> voidExpected = resultadoVacio;

        when(categoryService.getAllCategories(1, 0)).thenReturn(voidExpected);

        mockMvc.perform(get("/api/categories")
                .contentType("application/json")
                .param("size", "0").param("page", "1")
                .content(objectMapper.writeValueAsString(objectMapper.writeValueAsString(resultEmptyElement)))
        )
                .andExpect(result -> Page.empty());

    }
}