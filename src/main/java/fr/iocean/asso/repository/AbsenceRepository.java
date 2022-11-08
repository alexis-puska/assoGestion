package fr.iocean.asso.repository;

import fr.iocean.asso.domain.Absence;
import fr.iocean.asso.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Absence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    Page<Absence> findAllByUser(Pageable pageable, User user);
}
