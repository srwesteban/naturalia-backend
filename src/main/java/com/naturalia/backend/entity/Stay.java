package com.naturalia.backend.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private List<String> images;

    private String location;

    private int capacity;

    private double pricePerNight;

    @Enumerated(EnumType.STRING)
    private StayType type;

    public Stay() {
    }

    public Stay(String name, String description, List<String> images, String location, int capacity, double pricePerNight, StayType type) {
        this.name = name;
        this.description = description;
        this.images = images;
        this.location = location;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public StayType getType() {
        return type;
    }

    public void setType(StayType type) {
        this.type = type;
    }
}
