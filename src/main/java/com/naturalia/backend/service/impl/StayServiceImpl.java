package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.*;
import com.naturalia.backend.entity.*;
import com.naturalia.backend.exception.DuplicateNameException;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.mapper.StayMapper;
import com.naturalia.backend.repository.ICategoryRepository;
import com.naturalia.backend.repository.IFeatureRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IStayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StayServiceImpl implements IStayService {

    private final IStayRepository stayRepository;
    private final IFeatureRepository featureRepository;
    private final ICategoryRepository categoryRepository;
    private final IUserRepository userRepository;
    private final StayMapper stayMapper;


    @Override
    public Stay save(Stay stay) {
        if (stayRepository.existsByName(stay.getName())) {
            throw new DuplicateNameException("DUPLICATE_NAME");
        }
        return stayRepository.save(stay);
    }

    @Override
    public Optional<Stay> findById(Long id) {
        return stayRepository.findById(id);
    }

    @Override
    public void update(Stay stay) {
        if (!stayRepository.existsById(stay.getId())) {
            throw new ResourceNotFoundException("Stay not found with id: " + stay.getId());
        }
        stayRepository.save(stay);
    }

    @Override
    public void delete(Long id) {
        if (!stayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stay not found with id: " + id);
        }
        stayRepository.deleteById(id);
    }

    @Override
    public StayDTO create(StayRequest request) {
        User host;
        if (request.getHostId() != null) {
            host = userRepository.findById(request.getHostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Host no encontrado"));
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            host = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        }

        List<Feature> features = featureRepository.findAllById(
                request.getFeatureIds() != null ? request.getFeatureIds() : List.of()
        );

        List<Category> categories = categoryRepository.findAllById(
                request.getCategoryIds() != null ? request.getCategoryIds() : List.of()
        );

        Stay stay = Stay.builder()
                .name(request.getName())
                .description(request.getDescription())
                .images(request.getImages())
                .location(request.getLocation())
                .capacity(request.getCapacity())
                .pricePerNight(request.getPricePerNight())
                .bedrooms(request.getBedrooms())
                .beds(request.getBeds())
                .bathrooms(request.getBathrooms())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .features(features)
                .categories(categories)
                .host(host)
                .build();

        Stay saved = stayRepository.save(stay);
        return convertToDTO(saved);
    }

    @Override
    public StayDTO updateStay(Long id, StayRequest request) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found"));

        List<Feature> features = featureRepository.findAllById(
                request.getFeatureIds() != null ? request.getFeatureIds() : List.of()
        );

        List<Category> categories = categoryRepository.findAllById(
                request.getCategoryIds() != null ? request.getCategoryIds() : List.of()
        );

        stay.setName(request.getName());
        stay.setDescription(request.getDescription());
        stay.setImages(request.getImages());
        stay.setLocation(request.getLocation());
        stay.setCapacity(request.getCapacity());
        stay.setPricePerNight(request.getPricePerNight());
        stay.setBedrooms(request.getBedrooms());
        stay.setBeds(request.getBeds());
        stay.setBathrooms(request.getBathrooms());
        stay.setLatitude(request.getLatitude());
        stay.setLongitude(request.getLongitude());
        stay.setFeatures(features);
        stay.setCategories(categories);

        return convertToDTO(stayRepository.save(stay));
    }

    @Override
    public List<StayDTO> findAll() {
        return stayRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StayDTO findDTOById(Long id) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found with id: " + id));
        return convertToDTO(stay);
    }


    @Override
    public List<StayDTO> findAllDTOs() {
        return stayRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StayDTO convertToDTO(Stay stay) {

        double averageRating = 0.0;

        if (stay.getReviews() != null && !stay.getReviews().isEmpty()) {
            averageRating = stay.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
        }

        return StayDTO.builder()
                .id(stay.getId())
                .name(stay.getName())
                .description(stay.getDescription())
                .images(stay.getImages())
                .location(stay.getLocation())
                .capacity(stay.getCapacity())
                .pricePerNight(stay.getPricePerNight())
                .bedrooms(stay.getBedrooms())
                .beds(stay.getBeds())
                .bathrooms(stay.getBathrooms())
                .latitude(stay.getLatitude())
                .longitude(stay.getLongitude())
                .features(stay.getFeatures().stream()
                        .map(feature -> FeatureDTO.builder()
                                .id(feature.getId())
                                .name(feature.getName())
                                .icon(feature.getIcon())
                                .build())
                        .collect(Collectors.toList()))
                .host(UserDTO.builder()
                        .id(stay.getHost().getId())
                        .firstname(stay.getHost().getFirstname())
                        .build())
                .categories(stay.getCategories().stream()
                        .map(category -> CategoryDTO.builder()
                                .id(category.getId())
                                .title(category.getTitle())
                                .description(category.getDescription())
                                .imageUrl(category.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .averageRating(averageRating)
                .build();
    }


    public List<Stay> findAvailableStays(LocalDate checkIn, LocalDate checkOut) {
        return stayRepository.findAvailableStays(checkIn, checkOut);
    }

    @Override
    public List<StayDTO> getSuggestionsByName(String query) {
        return stayRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(stayMapper::toDTO)
                .toList();
    }

    @Override
    public List<StayDTO> findRecommended() {
        return stayRepository.findAll().stream()
                .filter(stay -> {
                    if (stay.getReviews() == null || stay.getReviews().isEmpty()) return false;
                    double avg = stay.getReviews().stream().mapToDouble(Review::getRating).average().orElse(0.0);
                    return avg >= 4.0;
                })
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<StaySummaryDTO> findAllSummaries() {
        List<Stay> stays = stayRepository.findAll();
        return stays.stream()
                .map(stay -> new StaySummaryDTO(
                        stay.getId(),
                        stay.getName()
                ))
                .toList();
    }

    @Override
    public List<StayListCardDTO> findAllListCards() {
        List<Stay> stays = stayRepository.findAllWithCategories();

        return stays.stream().map(stay -> {
            List<CategoryTitleDTO> categoryTitles = stay.getCategories().stream()
                    .map(cat -> new CategoryTitleDTO(cat.getTitle()))
                    .collect(Collectors.toList());

            return StayListCardDTO.builder()
                    .id(stay.getId())
                    .name(stay.getName())
                    .description(stay.getDescription())
                    .imageUrl(
                            stay.getImages() != null && !stay.getImages().isEmpty()
                                    ? stay.getImages().get(0)
                                    : null
                    )
                    .location(stay.getLocation())
                    .pricePerNight(stay.getPricePerNight())
                    .categories(categoryTitles)
                    .build();
        }).collect(Collectors.toList());
    }


    @Override
    public List<StayListCardDTO> searchAvailableLight(LocalDate checkIn, LocalDate checkOut) {
        List<Stay> stays = stayRepository.findAvailableStays(checkIn, checkOut);

        return stays.stream().map(stay -> StayListCardDTO.builder()
                .id(stay.getId())
                .name(stay.getName())
                .description(stay.getDescription())
                .imageUrl((stay.getImages() != null && !stay.getImages().isEmpty()) ? stay.getImages().get(0) : null)
                .location(stay.getLocation())
                .pricePerNight(stay.getPricePerNight())
                .build()
        ).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateStay(Long id, StayUpdateDTO dto) {
        Stay stay = stayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found"));

        stay.setName(dto.getName());
        stay.setDescription(dto.getDescription());
        stay.setLocation(dto.getLocation());
        stay.setImages(dto.getImages());
        stay.setCapacity(dto.getCapacity());
        stay.setPricePerNight(dto.getPricePerNight());
        stay.setBedrooms(dto.getBedrooms());
        stay.setBeds(dto.getBeds());
        stay.setBathrooms(dto.getBathrooms());
        stay.setLatitude(dto.getLatitude());
        stay.setLongitude(dto.getLongitude());

        if (dto.getHostId() != null) {
            User host = userRepository.findById(dto.getHostId())
                    .orElseThrow(() -> new ResourceNotFoundException("Host not found"));
            stay.setHost(host);
        }

        if (dto.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            stay.setCategories(categories);
        }

        if (dto.getFeatureIds() != null) {
            List<Feature> features = featureRepository.findAllById(dto.getFeatureIds());
            stay.setFeatures(features);
        }

        stayRepository.save(stay);
    }

    @Override
    public List<StayDTO> findByHostId(Long hostId) {
        List<Stay> stays = stayRepository.findByHostId(hostId);
        return stays.stream()
                .map(this::convertToDTO)
                .toList();
    }



}
