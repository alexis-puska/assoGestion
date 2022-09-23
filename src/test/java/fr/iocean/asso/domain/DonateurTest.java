package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Donateur.class);
        Donateur donateur1 = new Donateur();
        donateur1.setId(1L);
        Donateur donateur2 = new Donateur();
        donateur2.setId(donateur1.getId());
        assertThat(donateur1).isEqualTo(donateur2);
        donateur2.setId(2L);
        assertThat(donateur1).isNotEqualTo(donateur2);
        donateur1.setId(null);
        assertThat(donateur1).isNotEqualTo(donateur2);
    }
}
