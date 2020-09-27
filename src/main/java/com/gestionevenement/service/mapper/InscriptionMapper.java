package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.InscriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inscription} and its DTO {@link InscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActiviteMapper.class, ParticipantMapper.class})
public interface InscriptionMapper extends EntityMapper<InscriptionDTO, Inscription> {

    @Mapping(source = "activite.id", target = "activiteId")
    @Mapping(source = "activite.nom", target = "activiteNom")
    @Mapping(source = "participant.id", target = "participantId")
    @Mapping(source = "participant.nom", target = "participantNom")
    InscriptionDTO toDto(Inscription inscription);

    @Mapping(source = "activiteId", target = "activite")
    @Mapping(source = "participantId", target = "participant")
    Inscription toEntity(InscriptionDTO inscriptionDTO);

    default Inscription fromId(Long id) {
        if (id == null) {
            return null;
        }
        Inscription inscription = new Inscription();
        inscription.setId(id);
        return inscription;
    }
}
