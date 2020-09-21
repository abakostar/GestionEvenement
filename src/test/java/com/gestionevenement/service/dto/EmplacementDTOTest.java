package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class EmplacementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmplacementDTO.class);
        EmplacementDTO emplacementDTO1 = new EmplacementDTO();
        emplacementDTO1.setId(1L);
        EmplacementDTO emplacementDTO2 = new EmplacementDTO();
        assertThat(emplacementDTO1).isNotEqualTo(emplacementDTO2);
        emplacementDTO2.setId(emplacementDTO1.getId());
        assertThat(emplacementDTO1).isEqualTo(emplacementDTO2);
        emplacementDTO2.setId(2L);
        assertThat(emplacementDTO1).isNotEqualTo(emplacementDTO2);
        emplacementDTO1.setId(null);
        assertThat(emplacementDTO1).isNotEqualTo(emplacementDTO2);
    }
}
