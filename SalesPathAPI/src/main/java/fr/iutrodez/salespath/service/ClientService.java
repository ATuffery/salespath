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

    //public List<Client> GetClientsById(int id) {
    //    try {
    //        return clientRepository.findAllById(id);
//        } catch (Exception e) {
//            throw new RuntimeException("Error get clients by id : " + e.getMessage());
//        }
//    }

}