package fr.iutrodez.salespath.service;

import com.fasterxml.jackson.annotation.OptBoolean;
import fr.iutrodez.salespath.model.ItineraryStep;
import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.repository.IItineraryStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItineraryStepService {

    @Autowired
    private IItineraryStepRepository itineraryStepRepository;

    public void addStep(ItineraryStep itineraryStep) {
        // On vérifie qu'une clé n'existe pas déjà
        Optional<ItineraryStep> stepOpt = itineraryStepRepository.findByIds(itineraryStep.getIdItinerary(),
                                                                            itineraryStep.getIdClient());
        if (stepOpt.isPresent()) {
            throw new IllegalArgumentException("The step already exists");
        }

        try {
            itineraryStepRepository.save(itineraryStep);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the step : " + e.getMessage());
        }
    }

    public Optional<ItineraryStep[]> getSteps(String idItinerary) {
        Optional<ItineraryStep[]> stepOpt = itineraryStepRepository.findByIdItinerary(idItinerary);

        if (stepOpt.isPresent()) {
            return stepOpt;
        }

        return Optional.empty();
    }
}
