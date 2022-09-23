package fr.iocean.asso.repository;

import fr.iocean.asso.domain.ConfigurationDon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConfigurationDon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationDonRepository extends JpaRepository<ConfigurationDon, Long> {}
