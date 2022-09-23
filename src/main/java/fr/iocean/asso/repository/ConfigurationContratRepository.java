package fr.iocean.asso.repository;

import fr.iocean.asso.domain.ConfigurationContrat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConfigurationContrat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationContratRepository extends JpaRepository<ConfigurationContrat, Long> {}
