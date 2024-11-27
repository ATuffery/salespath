package fr.iutrodez.salespath.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import fr.iutrodez.salespath.model.Client;
import java.util.List;

// Utiliser une interface pour le repository
public interface IClientRepository extends MongoRepository<Client, String> {

}