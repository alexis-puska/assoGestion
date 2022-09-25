package fr.iocean.asso.repository;

import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.domain.PointCapture;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointCapture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointCaptureRepository extends JpaRepository<PointCapture, Long> {
    @Query(
        "FROM PointCapture p join p.adresseCapture ad " +
        "WHERE LOWER(p.nom) like LOWER(:search) " +
        "OR LOWER(ad.rue) like LOWER(:search) " +
        "OR LOWER(ad.codePostale) like LOWER(:search) " +
        "OR LOWER(ad.ville) like LOWER(:search) "
    )
    List<PointCapture> findAutocomplete(@Param("search") String search);
}
