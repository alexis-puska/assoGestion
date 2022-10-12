package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationAssoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationAsso.class);
        ConfigurationAsso configurationAsso1 = new ConfigurationAsso();
        configurationAsso1.setId(1L);
        ConfigurationAsso configurationAsso2 = new ConfigurationAsso();
        configurationAsso2.setId(configurationAsso1.getId());
        assertThat(configurationAsso1).isEqualTo(configurationAsso2);
        configurationAsso2.setId(2L);
        assertThat(configurationAsso1).isNotEqualTo(configurationAsso2);
        configurationAsso1.setId(null);
        assertThat(configurationAsso1).isNotEqualTo(configurationAsso2);
    }
}
