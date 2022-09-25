package fr.iocean.asso.repository;

import fr.iocean.asso.domain.FamilleAccueil;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FamilleAccueil entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilleAccueilRepository extends JpaRepository<FamilleAccueil, Long> {
    @Query(
        "FROM FamilleAccueil f join f.adresse ad " +
        "WHERE LOWER(f.nom) like LOWER(:search) " +
        "OR LOWER(ad.rue) like LOWER(:search) " +
        "OR LOWER(ad.codePostale) like LOWER(:search) " +
        "OR LOWER(ad.ville) like LOWER(:search) "
    )
    List<FamilleAccueil> findAutocomplete(@Param("search") String search);
}
