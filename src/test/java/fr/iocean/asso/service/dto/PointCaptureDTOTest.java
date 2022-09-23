package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointCaptureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointCaptureDTO.class);
        PointCaptureDTO pointCaptureDTO1 = new PointCaptureDTO();
        pointCaptureDTO1.setId(1L);
        PointCaptureDTO pointCaptureDTO2 = new PointCaptureDTO();
        assertThat(pointCaptureDTO1).isNotEqualTo(pointCaptureDTO2);
        pointCaptureDTO2.setId(pointCaptureDTO1.getId());
        assertThat(pointCaptureDTO1).isEqualTo(pointCaptureDTO2);
        pointCaptureDTO2.setId(2L);
        assertThat(pointCaptureDTO1).isNotEqualTo(pointCaptureDTO2);
        pointCaptureDTO1.setId(null);
        assertThat(pointCaptureDTO1).isNotEqualTo(pointCaptureDTO2);
    }
}
