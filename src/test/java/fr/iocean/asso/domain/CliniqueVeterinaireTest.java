package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CliniqueVeterinaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CliniqueVeterinaire.class);
        CliniqueVeterinaire cliniqueVeterinaire1 = new CliniqueVeterinaire();
        cliniqueVeterinaire1.setId(1L);
        CliniqueVeterinaire cliniqueVeterinaire2 = new CliniqueVeterinaire();
        cliniqueVeterinaire2.setId(cliniqueVeterinaire1.getId());
        assertThat(cliniqueVeterinaire1).isEqualTo(cliniqueVeterinaire2);
        cliniqueVeterinaire2.setId(2L);
        assertThat(cliniqueVeterinaire1).isNotEqualTo(cliniqueVeterinaire2);
        cliniqueVeterinaire1.setId(null);
        assertThat(cliniqueVeterinaire1).isNotEqualTo(cliniqueVeterinaire2);
    }
}
