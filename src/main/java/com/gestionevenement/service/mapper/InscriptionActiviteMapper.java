package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.InscriptionActiviteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InscriptionActivite} and its DTO {@link InscriptionActiviteDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActiviteMapper.class, ParticipantMapper.class})
public interface InscriptionActiviteMapper extends EntityMapper<InscriptionActiviteDTO, InscriptionActivite> {

    @Mapping(source = "activite.id", target = "activiteId")
    @Mapping(source = "activite.nom", target = "activiteNom")
    @Mapping(source = "participant.id", target = "participantId")
    @Mapping(source = "participant.login", target = "participantFirstName")
    InscriptionActiviteDTO toDto(InscriptionActivite inscriptionActivite);

    @Mapping(source = "activiteId", target = "activite")
    @Mapping(source = "participantId", target = "participant")
    InscriptionActivite toEntity(InscriptionActiviteDTO inscriptionActiviteDTO);

    default InscriptionActivite fromId(Long id) {
        if (id == null) {
            return null;
        }
        InscriptionActivite inscriptionActivite = new InscriptionActivite();
        inscriptionActivite.setId(id);
        return inscriptionActivite;
    }
}
