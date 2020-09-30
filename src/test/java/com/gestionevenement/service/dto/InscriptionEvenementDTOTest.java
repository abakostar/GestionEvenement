package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class InscriptionEvenementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionEvenementDTO.class);
        InscriptionEvenementDTO inscriptionEvenementDTO1 = new InscriptionEvenementDTO();
        inscriptionEvenementDTO1.setId(1L);
        InscriptionEvenementDTO inscriptionEvenementDTO2 = new InscriptionEvenementDTO();
        assertThat(inscriptionEvenementDTO1).isNotEqualTo(inscriptionEvenementDTO2);
        inscriptionEvenementDTO2.setId(inscriptionEvenementDTO1.getId());
        assertThat(inscriptionEvenementDTO1).isEqualTo(inscriptionEvenementDTO2);
        inscriptionEvenementDTO2.setId(2L);
        assertThat(inscriptionEvenementDTO1).isNotEqualTo(inscriptionEvenementDTO2);
        inscriptionEvenementDTO1.setId(null);
        assertThat(inscriptionEvenementDTO1).isNotEqualTo(inscriptionEvenementDTO2);
    }
}
