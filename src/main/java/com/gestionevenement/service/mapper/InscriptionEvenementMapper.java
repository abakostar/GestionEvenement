package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.InscriptionEvenementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InscriptionEvenement} and its DTO {@link InscriptionEvenementDTO}.
 */
@Mapper(componentModel = "spring", uses = {EvenementMapper.class, ParticipantMapper.class})
public interface InscriptionEvenementMapper extends EntityMapper<InscriptionEvenementDTO, InscriptionEvenement> {

    @Mapping(source = "evenement.id", target = "evenementId")
    @Mapping(source = "evenement.description", target = "evenementDescription")
    @Mapping(source = "participant.id", target = "participantId")
    @Mapping(source = "participant.nom", target = "participantNom")
    InscriptionEvenementDTO toDto(InscriptionEvenement inscriptionEvenement);

    @Mapping(source = "evenementId", target = "evenement")
    @Mapping(source = "participantId", target = "participant")
    InscriptionEvenement toEntity(InscriptionEvenementDTO inscriptionEvenementDTO);

    default InscriptionEvenement fromId(Long id) {
        if (id == null) {
            return null;
        }
        InscriptionEvenement inscriptionEvenement = new InscriptionEvenement();
        inscriptionEvenement.setId(id);
        return inscriptionEvenement;
    }
}
