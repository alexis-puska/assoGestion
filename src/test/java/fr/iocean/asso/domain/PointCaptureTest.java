package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PointCaptureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointCapture.class);
        PointCapture pointCapture1 = new PointCapture();
        pointCapture1.setId(1L);
        PointCapture pointCapture2 = new PointCapture();
        pointCapture2.setId(pointCapture1.getId());
        assertThat(pointCapture1).isEqualTo(pointCapture2);
        pointCapture2.setId(2L);
        assertThat(pointCapture1).isNotEqualTo(pointCapture2);
        pointCapture1.setId(null);
        assertThat(pointCapture1).isNotEqualTo(pointCapture2);
    }
}
