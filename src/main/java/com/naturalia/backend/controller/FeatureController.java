package com.naturalia.backend.controller;

import com.naturalia.backend.dto.FeatureDTO;
import com.naturalia.backend.dto.FeatureRequest;
import com.naturalia.backend.service.IFeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
public class FeatureController {

    private final IFeatureService featureService;

    @GetMapping
    public ResponseEntity<List<FeatureDTO>> getAll() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @PostMapping
    public ResponseEntity<FeatureDTO> create(@RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.createFeature(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureDTO> update(@PathVariable Long id, @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureService.updateFeature(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}
