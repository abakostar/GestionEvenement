package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class InscriptionActiviteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionActiviteDTO.class);
        InscriptionActiviteDTO inscriptionActiviteDTO1 = new InscriptionActiviteDTO();
        inscriptionActiviteDTO1.setId(1L);
        InscriptionActiviteDTO inscriptionActiviteDTO2 = new InscriptionActiviteDTO();
        assertThat(inscriptionActiviteDTO1).isNotEqualTo(inscriptionActiviteDTO2);
        inscriptionActiviteDTO2.setId(inscriptionActiviteDTO1.getId());
        assertThat(inscriptionActiviteDTO1).isEqualTo(inscriptionActiviteDTO2);
        inscriptionActiviteDTO2.setId(2L);
        assertThat(inscriptionActiviteDTO1).isNotEqualTo(inscriptionActiviteDTO2);
        inscriptionActiviteDTO1.setId(null);
        assertThat(inscriptionActiviteDTO1).isNotEqualTo(inscriptionActiviteDTO2);
    }
}
