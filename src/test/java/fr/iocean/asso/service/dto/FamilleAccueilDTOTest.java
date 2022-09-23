package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilleAccueilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilleAccueilDTO.class);
        FamilleAccueilDTO familleAccueilDTO1 = new FamilleAccueilDTO();
        familleAccueilDTO1.setId(1L);
        FamilleAccueilDTO familleAccueilDTO2 = new FamilleAccueilDTO();
        assertThat(familleAccueilDTO1).isNotEqualTo(familleAccueilDTO2);
        familleAccueilDTO2.setId(familleAccueilDTO1.getId());
        assertThat(familleAccueilDTO1).isEqualTo(familleAccueilDTO2);
        familleAccueilDTO2.setId(2L);
        assertThat(familleAccueilDTO1).isNotEqualTo(familleAccueilDTO2);
        familleAccueilDTO1.setId(null);
        assertThat(familleAccueilDTO1).isNotEqualTo(familleAccueilDTO2);
    }
}
