package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaysMapperTest {

    private PaysMapper paysMapper;

    @BeforeEach
    public void setUp() {
        paysMapper = new PaysMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(paysMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(paysMapper.fromId(null)).isNull();
    }
}
