package com.gestionevenement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CategorieMapperTest {

    private CategorieMapper categorieMapper;

    @BeforeEach
    public void setUp() {
        categorieMapper = new CategorieMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(categorieMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(categorieMapper.fromId(null)).isNull();
    }
}
