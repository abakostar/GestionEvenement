package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class EvenementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evenement.class);
        Evenement evenement1 = new Evenement();
        evenement1.setId(1L);
        Evenement evenement2 = new Evenement();
        evenement2.setId(evenement1.getId());
        assertThat(evenement1).isEqualTo(evenement2);
        evenement2.setId(2L);
        assertThat(evenement1).isNotEqualTo(evenement2);
        evenement1.setId(null);
        assertThat(evenement1).isNotEqualTo(evenement2);
    }
}
