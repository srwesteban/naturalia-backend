package com.naturalia.backend.controller;

import com.naturalia.backend.dto.*;
import com.naturalia.backend.entity.Stay;
import com.naturalia.backend.service.IStayService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stays")
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

    @PutMapping("/{id}/full")
    public ResponseEntity<Void> updateStay(@PathVariable Long id, @RequestBody StayUpdateDTO dto) {
        stayService.updateStay(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStay(@PathVariable Long id) {
        try {
            stayService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Este alojamiento tiene reservas activas y no puede eliminarse.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el alojamiento.");
        }
    }






    @GetMapping("/search")
    public ResponseEntity<List<Stay>> searchAvailableStays(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut
    ) {
        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
            return ResponseEntity.badRequest().build();
        }

        List<Stay> availableStays = stayService.findAvailableStays(checkIn, checkOut);
        return ResponseEntity.ok(availableStays);
    }

    @GetMapping("/search-light")
    public ResponseEntity<List<StayListCardDTO>> searchAvailableLight(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(stayService.searchAvailableLight(checkIn, checkOut));
    }


    @GetMapping("/suggestions")
    public ResponseEntity<List<StayDTO>> getSuggestions(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(stayService.getSuggestionsByName(query));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<StayDTO>> getRecommendedStays() {
        return ResponseEntity.ok(stayService.findRecommended());
    }

    @GetMapping("/summary")
    public ResponseEntity<List<StaySummaryDTO>> getStaySummaries() {
        return ResponseEntity.ok(stayService.findAllSummaries());
    }

    @GetMapping("/list-cards")
    public ResponseEntity<List<StayListCardDTO>> getStayListCards() {
        return ResponseEntity.ok(stayService.findAllListCards());
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<StayDTO>> getStaysByHost(@PathVariable Long hostId) {
        List<StayDTO> stays = stayService.findByHostId(hostId);
        return ResponseEntity.ok(stays);
    }


}
