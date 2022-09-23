package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationDonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationDonDTO.class);
        ConfigurationDonDTO configurationDonDTO1 = new ConfigurationDonDTO();
        configurationDonDTO1.setId(1L);
        ConfigurationDonDTO configurationDonDTO2 = new ConfigurationDonDTO();
        assertThat(configurationDonDTO1).isNotEqualTo(configurationDonDTO2);
        configurationDonDTO2.setId(configurationDonDTO1.getId());
        assertThat(configurationDonDTO1).isEqualTo(configurationDonDTO2);
        configurationDonDTO2.setId(2L);
        assertThat(configurationDonDTO1).isNotEqualTo(configurationDonDTO2);
        configurationDonDTO1.setId(null);
        assertThat(configurationDonDTO1).isNotEqualTo(configurationDonDTO2);
    }
}
