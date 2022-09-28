package fr.iocean.asso.repository;

import fr.iocean.asso.domain.CliniqueVeterinaire;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CliniqueVeterinaire entity.
 */
@Repository
public interface CliniqueVeterinaireRepository extends JpaRepository<CliniqueVeterinaire, Long> {
    @Query(
        "FROM CliniqueVeterinaire cli join cli.adresse ad " +
        "WHERE cli.actif IS TRUE " +
        "AND LOWER(cli.nom) like LOWER(:search) " +
        "OR LOWER(ad.rue) like LOWER(:search) " +
        "OR LOWER(ad.codePostale) like LOWER(:search) " +
        "OR LOWER(ad.ville) like LOWER(:search) "
    )
    List<CliniqueVeterinaire> findAutocomplete(@Param("search") String search);
}
