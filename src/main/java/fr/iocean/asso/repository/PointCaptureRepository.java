package fr.iocean.asso.repository;

import fr.iocean.asso.domain.PointCapture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PointCapture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointCaptureRepository extends JpaRepository<PointCapture, Long> {}
