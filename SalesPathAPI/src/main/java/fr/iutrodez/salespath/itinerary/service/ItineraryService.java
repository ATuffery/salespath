package fr.iutrodez.salespath.itinerary.service;

import fr.iutrodez.salespath.account.model.SalesPerson;
import fr.iutrodez.salespath.account.repository.IAccountRepository;
import fr.iutrodez.salespath.itinerarystep.model.ItineraryStep;
import fr.iutrodez.salespath.itinerarystep.repository.IItineraryStepRepository;
import fr.iutrodez.salespath.itinerarystep.service.ItineraryStepService;
import fr.iutrodez.salespath.itinerary.dto.ItineraryAddRequest;
import fr.iutrodez.salespath.itinerary.model.Itinerary;
import fr.iutrodez.salespath.itinerary.repository.IItineraryRepository;
import fr.iutrodez.salespath.utils.PathFinder;
import jakarta.transaction.Transactional;
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
    private IItineraryStepRepository itineraryStepRepository;

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

    @Transactional
    public boolean deleteItinerary(Long id) {
        Optional<Itinerary> itineraryOpt = itineraryRepository.findById(id);

        if (itineraryOpt.isEmpty()) {
            return false;
        }

        try {
            itineraryStepRepository.deleteByIdItinerary(String.valueOf(id));
            itineraryRepository.deleteById(id);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting itinerary: " + e.getMessage());
        }
    }
}
