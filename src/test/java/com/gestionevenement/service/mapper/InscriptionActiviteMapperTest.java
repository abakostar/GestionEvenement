package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InscriptionActiviteMapperTest {

    private InscriptionActiviteMapper inscriptionActiviteMapper;

    @BeforeEach
    public void setUp() {
        inscriptionActiviteMapper = new InscriptionActiviteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(inscriptionActiviteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(inscriptionActiviteMapper.fromId(null)).isNull();
    }
}
