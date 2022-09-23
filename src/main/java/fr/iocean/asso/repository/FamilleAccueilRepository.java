package fr.iocean.asso.repository;

import fr.iocean.asso.domain.FamilleAccueil;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FamilleAccueil entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilleAccueilRepository extends JpaRepository<FamilleAccueil, Long> {}
