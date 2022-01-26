package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.payload.AlbumResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAllAlbums {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    AlbumServiceImpl albumService;

    //Test: Comprobar que consigue todos los albums
    //entrada: albumService.getAllAlbums(1,10)
    //salida esperada: el test se realiza con exito
    @Test
    void getAllAlbums_success(){

        Album album = new Album();
        album.setTitle("Título");
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.setId(1l);
        albumResponse.setTitle("Título");

        Page<Album> resultado = new PageImpl<>(Arrays.asList(album));
        List<AlbumResponse> resultado2 = Arrays.asList(albumResponse);
        AlbumResponse[] albumResponseList = {albumResponse};


        PagedResponse<AlbumResponse> resultadoEsperado = new PagedResponse<>();
        resultadoEsperado.setContent(resultado2);
        resultadoEsperado.setTotalPages(1);
        resultadoEsperado.setTotalElements(1);
        resultadoEsperado.setLast(true);
        resultadoEsperado.setSize(1);

        Pageable pageable = PageRequest.of(0,10);

        when(albumRepository.findAll(any(Pageable.class))).thenReturn(resultado);
        when(modelMapper.map(any(), any())).thenReturn(albumResponseList);

        assertEquals(resultadoEsperado, albumService.getAllAlbums(1,10));


    }
}
