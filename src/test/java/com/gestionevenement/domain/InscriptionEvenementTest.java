package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class InscriptionEvenementTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionEvenement.class);
        InscriptionEvenement inscriptionEvenement1 = new InscriptionEvenement();
        inscriptionEvenement1.setId(1L);
        InscriptionEvenement inscriptionEvenement2 = new InscriptionEvenement();
        inscriptionEvenement2.setId(inscriptionEvenement1.getId());
        assertThat(inscriptionEvenement1).isEqualTo(inscriptionEvenement2);
        inscriptionEvenement2.setId(2L);
        assertThat(inscriptionEvenement1).isNotEqualTo(inscriptionEvenement2);
        inscriptionEvenement1.setId(null);
        assertThat(inscriptionEvenement1).isNotEqualTo(inscriptionEvenement2);
    }
}
