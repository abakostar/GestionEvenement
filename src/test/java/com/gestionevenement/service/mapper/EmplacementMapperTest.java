package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmplacementMapperTest {

    private EmplacementMapper emplacementMapper;

    @BeforeEach
    public void setUp() {
        emplacementMapper = new EmplacementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(emplacementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(emplacementMapper.fromId(null)).isNull();
    }
}
