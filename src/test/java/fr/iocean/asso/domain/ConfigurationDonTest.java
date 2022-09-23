package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationDonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationDon.class);
        ConfigurationDon configurationDon1 = new ConfigurationDon();
        configurationDon1.setId(1L);
        ConfigurationDon configurationDon2 = new ConfigurationDon();
        configurationDon2.setId(configurationDon1.getId());
        assertThat(configurationDon1).isEqualTo(configurationDon2);
        configurationDon2.setId(2L);
        assertThat(configurationDon1).isNotEqualTo(configurationDon2);
        configurationDon1.setId(null);
        assertThat(configurationDon1).isNotEqualTo(configurationDon2);
    }
}
