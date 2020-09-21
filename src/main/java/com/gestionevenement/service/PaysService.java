package com.gestionevenement.service;

import com.gestionevenement.service.dto.PaysDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.Pays}.
 */
public interface PaysService {

    /**
     * Save a pays.
     *
     * @param paysDTO the entity to save.
     * @return the persisted entity.
     */
    PaysDTO save(PaysDTO paysDTO);

    /**
     * Get all the pays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaysDTO> findAll(Pageable pageable);


    /**
     * Get the "id" pays.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaysDTO> findOne(Long id);

    /**
     * Delete the "id" pays.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
