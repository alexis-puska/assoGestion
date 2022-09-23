package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisiteVeterinaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisiteVeterinaire.class);
        VisiteVeterinaire visiteVeterinaire1 = new VisiteVeterinaire();
        visiteVeterinaire1.setId(1L);
        VisiteVeterinaire visiteVeterinaire2 = new VisiteVeterinaire();
        visiteVeterinaire2.setId(visiteVeterinaire1.getId());
        assertThat(visiteVeterinaire1).isEqualTo(visiteVeterinaire2);
        visiteVeterinaire2.setId(2L);
        assertThat(visiteVeterinaire1).isNotEqualTo(visiteVeterinaire2);
        visiteVeterinaire1.setId(null);
        assertThat(visiteVeterinaire1).isNotEqualTo(visiteVeterinaire2);
    }
}
