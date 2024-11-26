package fr.iutrodez.salespath.repository;

import fr.iutrodez.salespath.model.SalesPerson;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends JpaRepository<SalesPerson, Long> {
}
