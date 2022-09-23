package fr.iocean.asso.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.iocean.asso.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RaceChatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RaceChat.class);
        RaceChat raceChat1 = new RaceChat();
        raceChat1.setId(1L);
        RaceChat raceChat2 = new RaceChat();
        raceChat2.setId(raceChat1.getId());
        assertThat(raceChat1).isEqualTo(raceChat2);
        raceChat2.setId(2L);
        assertThat(raceChat1).isNotEqualTo(raceChat2);
        raceChat1.setId(null);
        assertThat(raceChat1).isNotEqualTo(raceChat2);
    }
}
