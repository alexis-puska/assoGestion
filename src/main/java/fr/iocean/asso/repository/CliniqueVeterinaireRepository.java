package fr.iocean.asso.repository;

import fr.iocean.asso.domain.CliniqueVeterinaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CliniqueVeterinaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CliniqueVeterinaireRepository extends JpaRepository<CliniqueVeterinaire, Long> {}
