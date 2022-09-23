package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaceChatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceChatDTO.class);
        RaceChatDTO raceChatDTO1 = new RaceChatDTO();
        raceChatDTO1.setId(1L);
        RaceChatDTO raceChatDTO2 = new RaceChatDTO();
        assertThat(raceChatDTO1).isNotEqualTo(raceChatDTO2);
        raceChatDTO2.setId(raceChatDTO1.getId());
        assertThat(raceChatDTO1).isEqualTo(raceChatDTO2);
        raceChatDTO2.setId(2L);
        assertThat(raceChatDTO1).isNotEqualTo(raceChatDTO2);
        raceChatDTO1.setId(null);
        assertThat(raceChatDTO1).isNotEqualTo(raceChatDTO2);
    }
}
