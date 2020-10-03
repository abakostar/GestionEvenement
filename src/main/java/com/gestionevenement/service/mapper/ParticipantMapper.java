package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.ParticipantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participant} and its DTO {@link ParticipantDTO}.
 */
@Mapper(componentModel = "spring", uses = {VilleMapper.class, EvenementMapper.class})
public interface ParticipantMapper extends EntityMapper<ParticipantDTO, Participant> {

    @Mapping(source = "ville.id", target = "villeId")
    @Mapping(source = "ville.nom", target = "villeNom")
    ParticipantDTO toDto(Participant participant);

    @Mapping(source = "villeId", target = "ville")
    @Mapping(target = "removeEvenement", ignore = true)
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
