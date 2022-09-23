package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationContratTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationContrat.class);
        ConfigurationContrat configurationContrat1 = new ConfigurationContrat();
        configurationContrat1.setId(1L);
        ConfigurationContrat configurationContrat2 = new ConfigurationContrat();
        configurationContrat2.setId(configurationContrat1.getId());
        assertThat(configurationContrat1).isEqualTo(configurationContrat2);
        configurationContrat2.setId(2L);
        assertThat(configurationContrat1).isNotEqualTo(configurationContrat2);
        configurationContrat1.setId(null);
        assertThat(configurationContrat1).isNotEqualTo(configurationContrat2);
    }
}
