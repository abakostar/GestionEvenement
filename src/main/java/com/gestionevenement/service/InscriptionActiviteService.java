package com.gestionevenement.service;

import com.gestionevenement.service.dto.InscriptionActiviteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.gestionevenement.domain.InscriptionActivite}.
 */
public interface InscriptionActiviteService {

    /**
     * Save a inscriptionActivite.
     *
     * @param inscriptionActiviteDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionActiviteDTO save(InscriptionActiviteDTO inscriptionActiviteDTO);

    /**
     * Get all the inscriptionActivites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionActiviteDTO> findAll(Pageable pageable);


    /**
     * Get the "id" inscriptionActivite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionActiviteDTO> findOne(Long id);

    /**
     * Delete the "id" inscriptionActivite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
