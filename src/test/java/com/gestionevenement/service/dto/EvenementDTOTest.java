package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class EvenementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvenementDTO.class);
        EvenementDTO evenementDTO1 = new EvenementDTO();
        evenementDTO1.setId(1L);
        EvenementDTO evenementDTO2 = new EvenementDTO();
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
        evenementDTO2.setId(evenementDTO1.getId());
        assertThat(evenementDTO1).isEqualTo(evenementDTO2);
        evenementDTO2.setId(2L);
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
        evenementDTO1.setId(null);
        assertThat(evenementDTO1).isNotEqualTo(evenementDTO2);
    }
}
