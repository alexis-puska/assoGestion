package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CliniqueVeterinaireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CliniqueVeterinaireDTO.class);
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO1 = new CliniqueVeterinaireDTO();
        cliniqueVeterinaireDTO1.setId(1L);
        CliniqueVeterinaireDTO cliniqueVeterinaireDTO2 = new CliniqueVeterinaireDTO();
        assertThat(cliniqueVeterinaireDTO1).isNotEqualTo(cliniqueVeterinaireDTO2);
        cliniqueVeterinaireDTO2.setId(cliniqueVeterinaireDTO1.getId());
        assertThat(cliniqueVeterinaireDTO1).isEqualTo(cliniqueVeterinaireDTO2);
        cliniqueVeterinaireDTO2.setId(2L);
        assertThat(cliniqueVeterinaireDTO1).isNotEqualTo(cliniqueVeterinaireDTO2);
        cliniqueVeterinaireDTO1.setId(null);
        assertThat(cliniqueVeterinaireDTO1).isNotEqualTo(cliniqueVeterinaireDTO2);
    }
}
