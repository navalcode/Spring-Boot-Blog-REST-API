package com.sopromadze.blogapi.serviceTest.album;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Album;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GetAlbum {

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    AlbumServiceImpl albumService;

    /*
    * Test: Comprobar que el servicio devuelve un ResponseEntity<Album>
    * Entrada: 1L (id contenido en el repositorio)
    * Salida esperada: ResponseEntity<Album>
    * */
    @Test
    void getAlbum_success(){
        Album album = new Album();
        ResponseEntity<Album> response = new ResponseEntity<>(album, HttpStatus.OK);

        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.of(album));
        assertEquals(albumService.getAlbum(1L), response);
    }


    /*
     * Test: Comprobar que el servicio devuelve una excepción not found
     * Entrada: 0L (id sin contenido en el repositorio)
     * Salida esperada: Not Found Exception
     * */
    @Test
    void getAlbum_fails(){
        Album album = new Album();
        when(albumRepository.findById(1L)).thenReturn(java.util.Optional.of(album));
        assertThrows(ResourceNotFoundException.class,()->albumService.getAlbum(0L));
    }
}
