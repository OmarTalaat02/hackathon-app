package com.masjidlink.backend.controller;

import com.masjidlink.backend.model.Mosque;
import com.masjidlink.backend.repository.MosqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// REMOVE THIS: import org.springframework.security.access.prepost.PreAuthorize; // For role-based access
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mosques")
public class MosqueController {

    private final MosqueRepository mosqueRepository;

    public MosqueController(MosqueRepository mosqueRepository) {
        this.mosqueRepository = mosqueRepository;
    }

    @GetMapping // Get all mosques
    public ResponseEntity<List<Mosque>> getAllMosques() {
        List<Mosque> mosques = mosqueRepository.findAll();
        return ResponseEntity.ok(mosques);
    }

    @GetMapping("/{id}") // Get mosque by ID
    public ResponseEntity<Mosque> getMosqueById(@PathVariable Long id) {
        Optional<Mosque> mosque = mosqueRepository.findById(id);
        return mosque.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Update mosque (now public)
    // REMOVE THIS: @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Mosque> updateMosque(@PathVariable Long id, @RequestBody Mosque mosqueDetails) {
        return mosqueRepository.findById(id)
                .map(mosque -> {
                    mosque.setName(mosqueDetails.getName());
                    mosque.setAddress(mosqueDetails.getAddress());
                    mosque.setCity(mosqueDetails.getCity());
                    mosque.setState(mosqueDetails.getState());
                    mosque.setZipCode(mosqueDetails.getZipCode());
                    mosque.setContactEmail(mosqueDetails.getContactEmail());
                    mosque.setPhoneNumber(mosqueDetails.getPhoneNumber());
                    mosque.setDescription(mosqueDetails.getDescription());
                    mosque.setLogoUrl(mosqueDetails.getLogoUrl());
                    Mosque updatedMosque = mosqueRepository.save(mosque);
                    return ResponseEntity.ok(updatedMosque);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping // Create new mosque (now public)
    public ResponseEntity<Mosque> createMosque(@RequestBody Mosque newMosque) {
        // Basic validation: ensure name is unique
        if (mosqueRepository.existsByName(newMosque.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Mosque savedMosque = mosqueRepository.save(newMosque);
        // Return 201 Created with the location of the new resource
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMosque);
    }

    @DeleteMapping("/{id}") // Delete mosque (now public)
    public ResponseEntity<Void> deleteMosque(@PathVariable Long id) {
        if (!mosqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mosqueRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}