package com.sopromadze.blogapi;


import com.sopromadze.blogapi.model.Album;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.AlbumRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VerifyMockito {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    AlbumServiceImpl albumService;


    @Test
    void testWithMockito_SuccessYaPorFin() {

        User user = new User();
        user.setUsername("user");
        user.setId(1L);


        Album album = new Album();
        album.setUser(user);
        album.setTitle("El album");


        Page<Album> pageResult = new PageImpl<>(Arrays.asList(album));

        PagedResponse<Album> result = new PagedResponse<>();
        result.setContent(pageResult.getContent());
        result.setTotalPages(1);
        result.setTotalElements(1);
        result.setLast(true);
        result.setSize(1);

        when(userRepository.getUserByName("user")).thenReturn(user);

        when(albumRepository.findByCreatedBy(any(Long.class), any(Pageable.class))).thenReturn(pageResult);

        assertEquals(result, albumService.getUserAlbums("user", 0, 10));


    }


}
