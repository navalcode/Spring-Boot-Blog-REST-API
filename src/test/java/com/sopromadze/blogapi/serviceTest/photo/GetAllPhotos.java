package com.sopromadze.blogapi.serviceTest.photo;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.Photo;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PhotoResponse;
import com.sopromadze.blogapi.repository.PhotoRepository;
import com.sopromadze.blogapi.service.impl.PhotoServiceImpl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllPhotos {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    PhotoServiceImpl photoService;

    Photo photo;
    Album album;
    PhotoResponse photoResponse;
    Page resultado;
    List<PhotoResponse> resultado2;
    PagedResponse<PhotoResponse> resultadoEsperado;


    @BeforeEach
    void init() {
        album = new Album();
        photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Titulo");
        photo.setUrl("Url");
        photo.setThumbnailUrl("ThumbnailUrl");
        photo.setAlbum(album);

        photoResponse = new PhotoResponse(1L,"Titulo", "Url", "ThumbnailUrl", album.getId());

        resultado = new PageImpl<>(Arrays.asList(photo));
        resultado2 = Arrays.asList(photoResponse);

        resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(resultado2);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

    }

    //Test: Comprobar que se consigue todas las photos
    //Entrada: photoService.getAllPhotos(1,10)
    //Salida esperada: PagedResponse<PhotoResponse>
    @DisplayName("Get all photos")
    @Test
    void getAllPhotos_success() {
        PhotoResponse[] photoResponseList = {photoResponse};
        when(photoRepository.findAll(any(Pageable.class))).thenReturn(resultado);
        when(modelMapper.map(any(), any())).thenReturn(photoResponseList);

        assertEquals(resultadoEsperado, photoService.getAllPhotos(1, 10));
    }

    //Test: Comprobar que devuelve un pageable vacio
    //Entrada: photoService.getAllPhotos(1,10)
    //Salida esperada: PagedResponse<PhotoResponse> vacio
    @DisplayName("Get all photos with 0 elements")
    @Test
    void getAllPhotos_fail() {
        resultado = Page.empty();
        PhotoResponse[] photoResponseList = {photoResponse};
        when(photoRepository.findAll(any(Pageable.class))).thenReturn(resultado);
        when(modelMapper.map(any(), any())).thenReturn(photoResponseList);

        assertEquals(0, photoService.getAllPhotos(1, 10).getTotalElements());
    }
}
