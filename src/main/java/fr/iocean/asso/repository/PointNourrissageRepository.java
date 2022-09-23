package fr.iocean.asso.repository;

import fr.iocean.asso.domain.PointNourrissage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointNourrissage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointNourrissageRepository extends JpaRepository<PointNourrissage, Long> {}
