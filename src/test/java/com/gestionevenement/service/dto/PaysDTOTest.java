package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class PaysDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaysDTO.class);
        PaysDTO paysDTO1 = new PaysDTO();
        paysDTO1.setId(1L);
        PaysDTO paysDTO2 = new PaysDTO();
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
        paysDTO2.setId(paysDTO1.getId());
        assertThat(paysDTO1).isEqualTo(paysDTO2);
        paysDTO2.setId(2L);
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
        paysDTO1.setId(null);
        assertThat(paysDTO1).isNotEqualTo(paysDTO2);
    }
}
