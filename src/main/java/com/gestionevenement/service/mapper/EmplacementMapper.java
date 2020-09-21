package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.EmplacementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emplacement} and its DTO {@link EmplacementDTO}.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class})
public interface EmplacementMapper extends EntityMapper<EmplacementDTO, Emplacement> {

    @Mapping(source = "ville.id", target = "villeId")
    @Mapping(source = "ville.nom", target = "villeNom")
    EmplacementDTO toDto(Emplacement emplacement);

    @Mapping(source = "villeId", target = "ville")
    Emplacement toEntity(EmplacementDTO emplacementDTO);

    default Emplacement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Emplacement emplacement = new Emplacement();
        emplacement.setId(id);
        return emplacement;
    }
}
