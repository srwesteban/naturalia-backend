package com.naturalia.backend.controller;

import com.naturalia.backend.dto.StayDTO;
import com.naturalia.backend.dto.StayRequest;
import com.naturalia.backend.exception.ResourceNotFoundException;
import com.naturalia.backend.service.IStayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stays")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StayController {

    private final IStayService stayService;

    public StayController(IStayService stayService) {
        this.stayService = stayService;
    }

    @PostMapping
    public ResponseEntity<StayDTO> createStay(@RequestBody StayRequest request) {
        StayDTO created = stayService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StayDTO>> getAllStays() {
        return ResponseEntity.ok(stayService.findAllDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StayDTO> getStayById(@PathVariable Long id) {
        StayDTO stay = stayService.findDTOById(id);
        return ResponseEntity.ok(stay);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StayDTO> updateStay(@PathVariable Long id, @RequestBody StayRequest request) {
        StayDTO updated = stayService.updateStay(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStay(@PathVariable Long id) {
        stayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StayDTO>> searchStays(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<StayDTO> results = stayService.search(location, startDate, endDate);
        return ResponseEntity.ok(results);
    }

}
