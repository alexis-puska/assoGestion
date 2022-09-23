package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActeVeterinaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActeVeterinaire.class);
        ActeVeterinaire acteVeterinaire1 = new ActeVeterinaire();
        acteVeterinaire1.setId(1L);
        ActeVeterinaire acteVeterinaire2 = new ActeVeterinaire();
        acteVeterinaire2.setId(acteVeterinaire1.getId());
        assertThat(acteVeterinaire1).isEqualTo(acteVeterinaire2);
        acteVeterinaire2.setId(2L);
        assertThat(acteVeterinaire1).isNotEqualTo(acteVeterinaire2);
        acteVeterinaire1.setId(null);
        assertThat(acteVeterinaire1).isNotEqualTo(acteVeterinaire2);
    }
}
