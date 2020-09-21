package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ActiviteMapperTest {

    private ActiviteMapper activiteMapper;

    @BeforeEach
    public void setUp() {
        activiteMapper = new ActiviteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(activiteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(activiteMapper.fromId(null)).isNull();
    }
}
