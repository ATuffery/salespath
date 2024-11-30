package fr.iutrodez.salespath.service;

import fr.iutrodez.salespath.repository.IClientRepository;
import fr.iutrodez.salespath.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private  IClientRepository clientRepository;

    public void CreateClient(Client client) {
        try {
            clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Error create new client : " + e.getMessage());
        }
    }

    public List<Client> GetClientsById(Long idPerson) {
        try {
            return clientRepository.findByIdPerson(idPerson);
        } catch (Exception e) {
           throw new RuntimeException("Error get clients by id : " + e.getMessage());
        }
    }

    public Client GetClientById(Long id) {
        try {
            return clientRepository.findById(id);
        } catch (Exception e){
            throw new RuntimeException("Error get client by id : " + e.getMessage());
        }
    }

}