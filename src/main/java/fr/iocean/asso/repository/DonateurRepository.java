package fr.iocean.asso.repository;

import fr.iocean.asso.domain.Donateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Donateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonateurRepository extends JpaRepository<Donateur, Long> {}
