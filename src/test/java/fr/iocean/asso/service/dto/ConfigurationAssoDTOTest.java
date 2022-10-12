package fr.iocean.asso.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationAssoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationAssoDTO.class);
        ConfigurationAssoDTO configurationAssoDTO1 = new ConfigurationAssoDTO();
        configurationAssoDTO1.setId(1L);
        ConfigurationAssoDTO configurationAssoDTO2 = new ConfigurationAssoDTO();
        assertThat(configurationAssoDTO1).isNotEqualTo(configurationAssoDTO2);
        configurationAssoDTO2.setId(configurationAssoDTO1.getId());
        assertThat(configurationAssoDTO1).isEqualTo(configurationAssoDTO2);
        configurationAssoDTO2.setId(2L);
        assertThat(configurationAssoDTO1).isNotEqualTo(configurationAssoDTO2);
        configurationAssoDTO1.setId(null);
        assertThat(configurationAssoDTO1).isNotEqualTo(configurationAssoDTO2);
    }
}
