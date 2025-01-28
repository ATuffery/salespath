package fr.iutrodez.salespath.controller;

import fr.iutrodez.salespath.model.Itinerary;
import fr.iutrodez.salespath.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value = "/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @PostMapping()
    public ResponseEntity<?> createItinerary(@RequestBody Itinerary itinerary) {
        try {
            itineraryService.createItinerary(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(201).body(Map.of("success", "Account created"));
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<?> getItineraryUser(@PathVariable String idUser) {
        try {
            return ResponseEntity.status(200).body(itineraryService.getItineraryUser(idUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<?> getItinerary(@PathVariable String id) {
        try {
            return ResponseEntity.status(200).body(itineraryService.getItinerary(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteItinerary(String id) {
        try {
            itineraryService.deleteItinerary(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(200).body(Map.of("success", "Account deleted"));
    }
}
