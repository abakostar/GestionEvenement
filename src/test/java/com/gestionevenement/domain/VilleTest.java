package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class VilleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ville.class);
        Ville ville1 = new Ville();
        ville1.setId(1L);
        Ville ville2 = new Ville();
        ville2.setId(ville1.getId());
        assertThat(ville1).isEqualTo(ville2);
        ville2.setId(2L);
        assertThat(ville1).isNotEqualTo(ville2);
        ville1.setId(null);
        assertThat(ville1).isNotEqualTo(ville2);
    }
}
