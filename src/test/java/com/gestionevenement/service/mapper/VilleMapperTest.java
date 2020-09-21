package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class VilleMapperTest {

    private VilleMapper villeMapper;

    @BeforeEach
    public void setUp() {
        villeMapper = new VilleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(villeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(villeMapper.fromId(null)).isNull();
    }
}
