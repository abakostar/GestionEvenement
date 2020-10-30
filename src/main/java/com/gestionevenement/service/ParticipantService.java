package com.gestionevenement.service;

import com.gestionevenement.domain.User;
import com.gestionevenement.service.dto.ParticipantActiviteDTO;
import com.gestionevenement.service.dto.ParticipantDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.Participant}.
 */
public interface ParticipantService {

    /**
     * Save a participant.
     *
     * @param participantDTO the entity to save.
     * @return the persisted entity.
     */
    ParticipantDTO save(ParticipantDTO participantDTO);

    /**
     * Get all the participants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticipantDTO> findAll(Pageable pageable);

    /**
     * Get all the participants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ParticipantDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" participant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticipantDTO> findOne(Long id);

    /**
     * Delete the "id" participant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ParticipantDTO> findByUser(User user);

    Optional<List<ParticipantActiviteDTO>> findAllParticipantActiviteByUser(User user, Long evenementId);
}
