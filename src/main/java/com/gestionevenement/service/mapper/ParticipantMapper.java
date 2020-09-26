package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.ParticipantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participant} and its DTO {@link ParticipantDTO}.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class, UserMapper.class})
public interface ParticipantMapper extends EntityMapper<ParticipantDTO, Participant> {

    @Mapping(source = "villeResidence.id", target = "villeResidenceId")
    @Mapping(source = "villeResidence.nom", target = "villeResidenceNom")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ParticipantDTO toDto(Participant participant);

    @Mapping(source = "villeResidenceId", target = "villeResidence")
    @Mapping(source = "userId", target = "user")
    Participant toEntity(ParticipantDTO participantDTO);

    default Participant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setId(id);
        return participant;
    }
}
