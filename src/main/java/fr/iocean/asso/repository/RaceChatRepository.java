package fr.iocean.asso.repository;

import fr.iocean.asso.domain.RaceChat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RaceChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaceChatRepository extends JpaRepository<RaceChat, Long> {}
