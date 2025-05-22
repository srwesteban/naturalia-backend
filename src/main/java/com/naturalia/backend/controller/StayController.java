package com.naturalia.backend.controller;

import com.naturalia.backend.dto.StaySummaryDTO;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.service.IStayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stays")
@CrossOrigin(origins = "*") // cambiar luego
public class StayController {

    private final IStayService stayService;

    public StayController(IStayService stayService) {
        this.stayService = stayService;
    }

    @PostMapping
    public ResponseEntity<Stay> createStay(@RequestBody Stay stay) {
        Stay created = stayService.save(stay);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Stay>> getAllStays() {
        return ResponseEntity.ok(stayService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stay> getStayById(@PathVariable Long id) {
        return stayService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStay(@PathVariable Long id) throws ResourceNotFoundException {
        stayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<List<StaySummaryDTO>> getStaySummaries() {
        return ResponseEntity.ok(stayService.findAllSummaries());
    }

}
