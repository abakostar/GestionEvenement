package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.ActiviteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activite} and its DTO {@link ActiviteDTO}.
 */
@Mapper(componentModel = "spring", uses = {EvenementMapper.class, EmplacementMapper.class})
public interface ActiviteMapper extends EntityMapper<ActiviteDTO, Activite> {

    @Mapping(source = "evenement.id", target = "evenementId")
    @Mapping(source = "evenement.code", target = "evenementCode")
    @Mapping(source = "emplacement.id", target = "emplacementId")
    @Mapping(source = "emplacement.code", target = "emplacementCode")
    ActiviteDTO toDto(Activite activite);

    @Mapping(source = "evenementId", target = "evenement")
    @Mapping(source = "emplacementId", target = "emplacement")
    Activite toEntity(ActiviteDTO activiteDTO);

    default Activite fromId(Long id) {
        if (id == null) {
            return null;
        }
        Activite activite = new Activite();
        activite.setId(id);
        return activite;
    }
}
