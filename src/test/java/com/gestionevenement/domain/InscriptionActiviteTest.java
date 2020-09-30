package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class InscriptionActiviteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionActivite.class);
        InscriptionActivite inscriptionActivite1 = new InscriptionActivite();
        inscriptionActivite1.setId(1L);
        InscriptionActivite inscriptionActivite2 = new InscriptionActivite();
        inscriptionActivite2.setId(inscriptionActivite1.getId());
        assertThat(inscriptionActivite1).isEqualTo(inscriptionActivite2);
        inscriptionActivite2.setId(2L);
        assertThat(inscriptionActivite1).isNotEqualTo(inscriptionActivite2);
        inscriptionActivite1.setId(null);
        assertThat(inscriptionActivite1).isNotEqualTo(inscriptionActivite2);
    }
}
