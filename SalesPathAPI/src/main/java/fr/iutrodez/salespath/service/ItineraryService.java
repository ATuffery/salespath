package fr.iutrodez.salespath.service;

import fr.iutrodez.salespath.dto.ItineraryAddRequest;
import fr.iutrodez.salespath.model.Itinerary;
import fr.iutrodez.salespath.model.ItineraryStep;
import fr.iutrodez.salespath.model.SalesPerson;
import fr.iutrodez.salespath.repository.IAccountRepository;
import fr.iutrodez.salespath.repository.IItineraryRepository;
import fr.iutrodez.salespath.utils.PathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItineraryService {

    @Autowired
    private IItineraryRepository itineraryRepository;

    @Autowired
    private PathFinder pf;

    @Autowired
    private ItineraryStepService itineraryStepService;

    @Autowired
    private IAccountRepository accountRepository;

    public void createItinerary(ItineraryAddRequest iti) {
        try {
            String[] order = pf.itineraryOrder(iti.getIdClients(),
                                               Long.parseLong(iti.getItinerary().getCodeUser()));

            Itinerary saved = itineraryRepository.save(iti.getItinerary());

            for (int i = 0; i < order.length; i++) {
                ItineraryStep itiStep = new ItineraryStep();
                itiStep.setIdItinerary(String.valueOf(saved.getIdItinerary()));
                itiStep.setIdClient(order[i]);
                itiStep.setStep(i+1);

                itineraryStepService.addStep(itiStep);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("The step already exists");
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the itinerary : " + e.getMessage());
        }
    }

    public Optional<Itinerary[]> getItineraryUser(Long idUser) {
        SalesPerson existing = accountRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for ID : " + idUser));

        Optional<Itinerary[]> itineraryOpt = itineraryRepository.findByIdUser(idUser);

        if (itineraryOpt.isPresent()) {
            return itineraryOpt;
        } else {
           return Optional.empty();
        }
    }

    public Optional<Itinerary> getItinerary(Long id) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(id);

        if (itineraryOpt.isPresent()) {
            return itineraryOpt;
        } else {
            return Optional.empty();
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
