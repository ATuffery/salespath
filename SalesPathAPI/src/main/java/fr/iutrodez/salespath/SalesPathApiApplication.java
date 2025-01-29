package fr.iutrodez.salespath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application
 */
@SpringBootApplication()
public class SalesPathApiApplication {

    /**
     * Méthode principale de l'application
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(SalesPathApiApplication.class, args);
    }
}
