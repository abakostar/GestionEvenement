package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InscriptionEvenementMapperTest {

    private InscriptionEvenementMapper inscriptionEvenementMapper;

    @BeforeEach
    public void setUp() {
        inscriptionEvenementMapper = new InscriptionEvenementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(inscriptionEvenementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(inscriptionEvenementMapper.fromId(null)).isNull();
    }
}
