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
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllPhotosByAlbum {

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
        album.setId(1L);
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

    @DisplayName("Get all photos by album")
    @Test
    void getAllPhotosByAlbum_success() {

        PhotoResponse[] photoResponseList = {photoResponse};
        when(photoRepository.findByAlbumId(album.getId(), any(Pageable.class))).thenReturn(resultado);
        when(modelMapper.map(any(), any())).thenReturn(photoResponseList);

        assertEquals(resultadoEsperado, photoService.getAllPhotosByAlbum(album.getId(), 1, 10));
    }
}
