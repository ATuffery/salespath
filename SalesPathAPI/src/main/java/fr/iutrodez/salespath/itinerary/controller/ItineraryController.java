package fr.iutrodez.salespath.itinerary.controller;

import fr.iutrodez.salespath.itinerary.dto.ItineraryAddRequest;
import fr.iutrodez.salespath.itinerary.dto.ItineraryInfos;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.service.ItineraryService;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/itinerary")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @PostMapping()
    public ResponseEntity<?> createItinerary(@RequestBody ItineraryAddRequest itinerary) {
        try {
            itineraryService.createItinerary(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(201).body(Map.of("success", "Itinerary created"));
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<?> getItineraryUser(@PathVariable Long idUser) {
        try {
            Optional<Itinerary[]> itinerariesOpt = itineraryService.getItineraryUser(idUser);

            Itinerary[] itineraries = itinerariesOpt.get();
            return ResponseEntity.status(200).body(itineraries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Id user not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getInfos/{id}")
    public ResponseEntity<?> getInfos(@PathVariable Long id) {
        try {
            Optional<Itinerary> itineraryOpt = itineraryService.getItinerary(id);
            Optional<ItineraryStep[]> stepsOpt;

            if (itineraryOpt.isPresent()) {
                Itinerary itinerary = itineraryOpt.get();
                stepsOpt = itineraryStepService.getSteps(String.valueOf(itinerary.getIdItinerary()));

                if (stepsOpt.isPresent()) {
                    ItineraryStep[] steps = stepsOpt.get();

                    ItineraryInfos infos = new ItineraryInfos();
                    infos.setItinerary(itinerary);
                    infos.setSteps(steps);

                    return ResponseEntity.status(200).body(infos);
                } else {
                    return ResponseEntity.status(404).body(Map.of("error", "No steps found for this itinerary"));
                }
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Itinerary not found for this Id"));
            }
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
