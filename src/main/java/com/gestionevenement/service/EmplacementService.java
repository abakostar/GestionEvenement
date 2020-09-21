package com.gestionevenement.service;

import com.gestionevenement.service.dto.EmplacementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.Emplacement}.
 */
public interface EmplacementService {

    /**
     * Save a emplacement.
     *
     * @param emplacementDTO the entity to save.
     * @return the persisted entity.
     */
    EmplacementDTO save(EmplacementDTO emplacementDTO);

    /**
     * Get all the emplacements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmplacementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" emplacement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmplacementDTO> findOne(Long id);

    /**
     * Delete the "id" emplacement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
