package fr.iocean.asso.repository;

import fr.iocean.asso.domain.ConfigurationAsso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ConfigurationAsso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationAssoRepository extends JpaRepository<ConfigurationAsso, Long> {}
