package com.gestionevenement.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.gestionevenement.web.rest.TestUtil;

public class CategorieTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categorie.class);
        Categorie categorie1 = new Categorie();
        categorie1.setId(1L);
        Categorie categorie2 = new Categorie();
        categorie2.setId(categorie1.getId());
        assertThat(categorie1).isEqualTo(categorie2);
        categorie2.setId(2L);
        assertThat(categorie1).isNotEqualTo(categorie2);
        categorie1.setId(null);
        assertThat(categorie1).isNotEqualTo(categorie2);
    }
}
