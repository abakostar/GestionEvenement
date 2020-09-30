package com.gestionevenement.service;

import com.gestionevenement.service.dto.InscriptionEvenementDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.InscriptionEvenement}.
 */
public interface InscriptionEvenementService {

    /**
     * Save a inscriptionEvenement.
     *
     * @param inscriptionEvenementDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionEvenementDTO save(InscriptionEvenementDTO inscriptionEvenementDTO);

    /**
     * Get all the inscriptionEvenements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionEvenementDTO> findAll(Pageable pageable);


    /**
     * Get the "id" inscriptionEvenement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionEvenementDTO> findOne(Long id);

    /**
     * Delete the "id" inscriptionEvenement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
