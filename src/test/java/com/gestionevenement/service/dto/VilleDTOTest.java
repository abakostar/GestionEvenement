package com.gestionevenement.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class VilleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VilleDTO.class);
        VilleDTO villeDTO1 = new VilleDTO();
        villeDTO1.setId(1L);
        VilleDTO villeDTO2 = new VilleDTO();
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
        villeDTO2.setId(villeDTO1.getId());
        assertThat(villeDTO1).isEqualTo(villeDTO2);
        villeDTO2.setId(2L);
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
        villeDTO1.setId(null);
        assertThat(villeDTO1).isNotEqualTo(villeDTO2);
    }
}
