package fr.iocean.asso.repository;

import fr.iocean.asso.domain.FamilleAccueil;
import fr.iocean.asso.domain.RaceChat;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RaceChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceChatRepository extends JpaRepository<RaceChat, Long> {
    @Query("FROM RaceChat f WHERE LOWER(f.libelle) like LOWER(:search)")
    List<RaceChat> findAutocomplete(@Param("search") String search);
}
