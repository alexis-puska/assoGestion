package fr.iocean.asso.repository;

import fr.iocean.asso.domain.VisiteVeterinaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VisiteVeterinaire entity.
 */
@Repository
public interface VisiteVeterinaireRepository extends JpaRepository<VisiteVeterinaire, Long> {}
