package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointNourrissageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointNourrissageDTO.class);
        PointNourrissageDTO pointNourrissageDTO1 = new PointNourrissageDTO();
        pointNourrissageDTO1.setId(1L);
        PointNourrissageDTO pointNourrissageDTO2 = new PointNourrissageDTO();
        assertThat(pointNourrissageDTO1).isNotEqualTo(pointNourrissageDTO2);
        pointNourrissageDTO2.setId(pointNourrissageDTO1.getId());
        assertThat(pointNourrissageDTO1).isEqualTo(pointNourrissageDTO2);
        pointNourrissageDTO2.setId(2L);
        assertThat(pointNourrissageDTO1).isNotEqualTo(pointNourrissageDTO2);
        pointNourrissageDTO1.setId(null);
        assertThat(pointNourrissageDTO1).isNotEqualTo(pointNourrissageDTO2);
    }
}
