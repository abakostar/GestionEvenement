package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EvenementMapperTest {

    private EvenementMapper evenementMapper;

    @BeforeEach
    public void setUp() {
        evenementMapper = new EvenementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(evenementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(evenementMapper.fromId(null)).isNull();
    }
}
