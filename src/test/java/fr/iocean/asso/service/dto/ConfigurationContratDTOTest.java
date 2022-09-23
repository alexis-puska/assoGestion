package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationContratDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationContratDTO.class);
        ConfigurationContratDTO configurationContratDTO1 = new ConfigurationContratDTO();
        configurationContratDTO1.setId(1L);
        ConfigurationContratDTO configurationContratDTO2 = new ConfigurationContratDTO();
        assertThat(configurationContratDTO1).isNotEqualTo(configurationContratDTO2);
        configurationContratDTO2.setId(configurationContratDTO1.getId());
        assertThat(configurationContratDTO1).isEqualTo(configurationContratDTO2);
        configurationContratDTO2.setId(2L);
        assertThat(configurationContratDTO1).isNotEqualTo(configurationContratDTO2);
        configurationContratDTO1.setId(null);
        assertThat(configurationContratDTO1).isNotEqualTo(configurationContratDTO2);
    }
}
