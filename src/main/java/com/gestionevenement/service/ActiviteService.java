package com.gestionevenement.service;

import com.gestionevenement.service.dto.ActiviteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.Activite}.
 */
public interface ActiviteService {

    /**
     * Save a activite.
     *
     * @param activiteDTO the entity to save.
     * @return the persisted entity.
     */
    ActiviteDTO save(ActiviteDTO activiteDTO);

    /**
     * Get all the activites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ActiviteDTO> findAll(Pageable pageable);


    /**
     * Get the "id" activite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActiviteDTO> findOne(Long id);

    /**
     * Delete the "id" activite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Boolean placedispo(Long id);
}
