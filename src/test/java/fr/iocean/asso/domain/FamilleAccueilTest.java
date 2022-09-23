package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilleAccueilTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleAccueil.class);
        FamilleAccueil familleAccueil1 = new FamilleAccueil();
        familleAccueil1.setId(1L);
        FamilleAccueil familleAccueil2 = new FamilleAccueil();
        familleAccueil2.setId(familleAccueil1.getId());
        assertThat(familleAccueil1).isEqualTo(familleAccueil2);
        familleAccueil2.setId(2L);
        assertThat(familleAccueil1).isNotEqualTo(familleAccueil2);
        familleAccueil1.setId(null);
        assertThat(familleAccueil1).isNotEqualTo(familleAccueil2);
    }
}
