package fr.iocean.asso.repository;

import fr.iocean.asso.domain.ActeVeterinaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ActeVeterinaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActeVeterinaireRepository extends JpaRepository<ActeVeterinaire, Long> {}
