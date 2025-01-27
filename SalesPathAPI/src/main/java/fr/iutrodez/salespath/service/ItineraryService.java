package fr.iutrodez.salespath.service;

import fr.iutrodez.salespath.model.Itinerary;
import fr.iutrodez.salespath.repository.IItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItineraryService {

    @Autowired
    private IItineraryRepository itineraryRepository;

    public Itinerary createItinerary(Itinerary iti) {
        try {

            return itineraryRepository.save(iti);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the itinerary : " + e.getMessage());
        }
    }

    public Optional<Itinerary> getItineraryUser(String idUser) {
        try {
            return itineraryRepository.findByIdUser(idUser);
        } catch (Exception e) {
            throw new RuntimeException("Error while search the itinerarys : " + e.getMessage());
        }
    }

    public Optional<Itinerary> getItinerary(String id) {
        try {
            return itineraryRepository.findById(Long.parseLong(id));
        } catch (Exception e) {
            throw new RuntimeException("Error while search the itinerary : " + e.getMessage());
        }
    }

    public void deleteItinerary(String id) {
        try {
            itineraryRepository.deleteById(Long.parseLong(id));
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting the itinerary : " + e.getMessage());
        }
    }
}
