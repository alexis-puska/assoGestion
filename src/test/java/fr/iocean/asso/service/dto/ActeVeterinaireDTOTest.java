package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActeVeterinaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActeVeterinaireDTO.class);
        ActeVeterinaireDTO acteVeterinaireDTO1 = new ActeVeterinaireDTO();
        acteVeterinaireDTO1.setId(1L);
        ActeVeterinaireDTO acteVeterinaireDTO2 = new ActeVeterinaireDTO();
        assertThat(acteVeterinaireDTO1).isNotEqualTo(acteVeterinaireDTO2);
        acteVeterinaireDTO2.setId(acteVeterinaireDTO1.getId());
        assertThat(acteVeterinaireDTO1).isEqualTo(acteVeterinaireDTO2);
        acteVeterinaireDTO2.setId(2L);
        assertThat(acteVeterinaireDTO1).isNotEqualTo(acteVeterinaireDTO2);
        acteVeterinaireDTO1.setId(null);
        assertThat(acteVeterinaireDTO1).isNotEqualTo(acteVeterinaireDTO2);
    }
}
