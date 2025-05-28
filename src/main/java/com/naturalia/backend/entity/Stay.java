package com.naturalia.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stays")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "stay_images", joinColumns = @JoinColumn(name = "stay_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    private String location;

    private int capacity;

    private double pricePerNight;

    @Enumerated(EnumType.STRING)
    private StayType type;

    @ManyToMany
    @JoinTable(
            name = "stay_features",
            joinColumns = @JoinColumn(name = "stay_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<Feature> features = new ArrayList<>();
}
