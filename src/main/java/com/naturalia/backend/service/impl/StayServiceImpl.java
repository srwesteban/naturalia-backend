package com.naturalia.backend.service.impl;

import com.naturalia.backend.dto.*;
import com.naturalia.backend.entity.Category;
import com.naturalia.backend.entity.Feature;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.entity.User;
import com.naturalia.backend.exception.DuplicateNameException;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.repository.ICategoryRepository;
import com.naturalia.backend.repository.IFeatureRepository;
import com.naturalia.backend.repository.IStayRepository;
import com.naturalia.backend.repository.IUserRepository;
import com.naturalia.backend.service.IStayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
                .categories(stay.getCategories().stream()
                        .map(category -> CategoryDTO.builder()
                                .id(category.getId())
                                .title(category.getTitle())
                                .description(category.getDescription())
                                .imageUrl(category.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<StayDTO> search(String location, LocalDate startDate, LocalDate endDate) {
        List<Stay> stays;

        if (startDate != null && endDate != null) {
            stays = stayRepository.findAvailableStays(location, startDate, endDate);
        } else {
            stays = stayRepository.findByLocationContainingIgnoreCase(location == null ? "" : location);
        }

        return stays.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


}
