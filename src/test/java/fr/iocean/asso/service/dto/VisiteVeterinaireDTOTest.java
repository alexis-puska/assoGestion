package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisiteVeterinaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisiteVeterinaireDTO.class);
        VisiteVeterinaireDTO visiteVeterinaireDTO1 = new VisiteVeterinaireDTO();
        visiteVeterinaireDTO1.setId(1L);
        VisiteVeterinaireDTO visiteVeterinaireDTO2 = new VisiteVeterinaireDTO();
        assertThat(visiteVeterinaireDTO1).isNotEqualTo(visiteVeterinaireDTO2);
        visiteVeterinaireDTO2.setId(visiteVeterinaireDTO1.getId());
        assertThat(visiteVeterinaireDTO1).isEqualTo(visiteVeterinaireDTO2);
        visiteVeterinaireDTO2.setId(2L);
        assertThat(visiteVeterinaireDTO1).isNotEqualTo(visiteVeterinaireDTO2);
        visiteVeterinaireDTO1.setId(null);
        assertThat(visiteVeterinaireDTO1).isNotEqualTo(visiteVeterinaireDTO2);
    }
}
