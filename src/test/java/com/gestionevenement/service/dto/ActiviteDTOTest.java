package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class ActiviteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiviteDTO.class);
        ActiviteDTO activiteDTO1 = new ActiviteDTO();
        activiteDTO1.setId(1L);
        ActiviteDTO activiteDTO2 = new ActiviteDTO();
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
        activiteDTO2.setId(activiteDTO1.getId());
        assertThat(activiteDTO1).isEqualTo(activiteDTO2);
        activiteDTO2.setId(2L);
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
        activiteDTO1.setId(null);
        assertThat(activiteDTO1).isNotEqualTo(activiteDTO2);
    }
}
