package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonateurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonateurDTO.class);
        DonateurDTO donateurDTO1 = new DonateurDTO();
        donateurDTO1.setId(1L);
        DonateurDTO donateurDTO2 = new DonateurDTO();
        assertThat(donateurDTO1).isNotEqualTo(donateurDTO2);
        donateurDTO2.setId(donateurDTO1.getId());
        assertThat(donateurDTO1).isEqualTo(donateurDTO2);
        donateurDTO2.setId(2L);
        assertThat(donateurDTO1).isNotEqualTo(donateurDTO2);
        donateurDTO1.setId(null);
        assertThat(donateurDTO1).isNotEqualTo(donateurDTO2);
    }
}
