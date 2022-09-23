package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointNourrissageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointNourrissage.class);
        PointNourrissage pointNourrissage1 = new PointNourrissage();
        pointNourrissage1.setId(1L);
        PointNourrissage pointNourrissage2 = new PointNourrissage();
        pointNourrissage2.setId(pointNourrissage1.getId());
        assertThat(pointNourrissage1).isEqualTo(pointNourrissage2);
        pointNourrissage2.setId(2L);
        assertThat(pointNourrissage1).isNotEqualTo(pointNourrissage2);
        pointNourrissage1.setId(null);
        assertThat(pointNourrissage1).isNotEqualTo(pointNourrissage2);
    }
}
