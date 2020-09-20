package com.gestionevenement.service;

import com.gestionevenement.service.dto.EvenementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.Evenement}.
 */
public interface EvenementService {

    /**
     * Save a evenement.
     *
     * @param evenementDTO the entity to save.
     * @return the persisted entity.
     */
    EvenementDTO save(EvenementDTO evenementDTO);

    /**
     * Get all the evenements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvenementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" evenement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvenementDTO> findOne(Long id);

    /**
     * Delete the "id" evenement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
