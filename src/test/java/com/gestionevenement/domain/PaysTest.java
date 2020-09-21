package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class PaysTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pays.class);
        Pays pays1 = new Pays();
        pays1.setId(1L);
        Pays pays2 = new Pays();
        pays2.setId(pays1.getId());
        assertThat(pays1).isEqualTo(pays2);
        pays2.setId(2L);
        assertThat(pays1).isNotEqualTo(pays2);
        pays1.setId(null);
        assertThat(pays1).isNotEqualTo(pays2);
    }
}
