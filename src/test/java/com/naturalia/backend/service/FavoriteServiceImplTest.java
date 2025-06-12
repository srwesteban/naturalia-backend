package com.naturalia.backend.service;

import com.naturalia.backend.dto.FavoriteDTO;
import com.naturalia.backend.entity.Favorite;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.repository.IFavoriteRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceImplTest {

    @Mock
    private IFavoriteRepository favoriteRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IStayRepository stayRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private User user;
    private Stay stay;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).build();
        stay = Stay.builder().id(10L).name("Stay 1").location("Loc").images(List.of("img1")).build();
        favorite = Favorite.builder().id(100L).user(user).stay(stay).build();
    }

    @Test
    void save_newFavorite_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(favoriteRepository.findByUserIdAndStayId(user.getId(), stay.getId())).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        Favorite saved = favoriteService.save(user.getId(), stay.getId());

        assertNotNull(saved);
        assertEquals(user.getId(), saved.getUser().getId());
        assertEquals(stay.getId(), saved.getStay().getId());

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void save_existingFavorite_returnsNull() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(favoriteRepository.findByUserIdAndStayId(user.getId(), stay.getId())).thenReturn(Optional.of(favorite));

        Favorite saved = favoriteService.save(user.getId(), stay.getId());

        assertNull(saved);
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void remove_callsRepository() {
        doNothing().when(favoriteRepository).deleteByUserIdAndStayId(user.getId(), stay.getId());

        favoriteService.remove(user.getId(), stay.getId());

        verify(favoriteRepository).deleteByUserIdAndStayId(user.getId(), stay.getId());
    }

    @Test
    void getFavoritesByUser_returnsDTOs() {
        when(favoriteRepository.findByUserId(user.getId())).thenReturn(List.of(favorite));

        List<FavoriteDTO> dtos = favoriteService.getFavoritesByUser(user.getId());

        assertEquals(1, dtos.size());
        FavoriteDTO dto = dtos.get(0);
        assertEquals(favorite.getId(), dto.getId());
        assertEquals(user.getId(), dto.getUserId());
        assertEquals(stay.getId(), dto.getStayId());
        assertEquals(stay.getName(), dto.getStayName());
        assertEquals(stay.getImages().get(0), dto.getStayImage());
        assertEquals(stay.getLocation(), dto.getLocation());
    }

    @Test
    void isFavorite_true() {
        when(favoriteRepository.findByUserIdAndStayId(user.getId(), stay.getId())).thenReturn(Optional.of(favorite));

        assertTrue(favoriteService.isFavorite(user.getId(), stay.getId()));
    }

    @Test
    void isFavorite_false() {
        when(favoriteRepository.findByUserIdAndStayId(user.getId(), stay.getId())).thenReturn(Optional.empty());

        assertFalse(favoriteService.isFavorite(user.getId(), stay.getId()));
    }

    @Test
    void addFavorite_savesFavorite() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(stayRepository.findById(stay.getId())).thenReturn(Optional.of(stay));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        favoriteService.addFavorite(user.getId(), stay.getId());

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_userNotFound_throws() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            favoriteService.addFavorite(user.getId(), stay.getId());
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void addFavorite_stayNotFound_throws() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(stayRepository.findById(stay.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            favoriteService.addFavorite(user.getId(), stay.getId());
        });

        assertEquals("Alojamiento no encontrado", ex.getMessage());
    }
}
