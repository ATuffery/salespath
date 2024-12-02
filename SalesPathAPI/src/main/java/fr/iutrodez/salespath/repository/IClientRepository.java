package fr.iutrodez.salespath.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import fr.iutrodez.salespath.model.Client;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

// Utiliser une interface pour le repository
public interface IClientRepository extends MongoRepository<Client, String>{

    List<Client> findByIdPerson(Long idPerson);

    Client findById(Long id);

}