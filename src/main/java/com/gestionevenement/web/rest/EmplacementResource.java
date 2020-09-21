package com.gestionevenement.web.rest;

import com.gestionevenement.service.EmplacementService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.EmplacementDTO;
import com.gestionevenement.service.dto.EmplacementCriteria;
import com.gestionevenement.service.EmplacementQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gestionevenement.domain.Emplacement}.
 */
@RestController
@RequestMapping("/api")
public class EmplacementResource {

    private final Logger log = LoggerFactory.getLogger(EmplacementResource.class);

    private static final String ENTITY_NAME = "emplacement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmplacementService emplacementService;

    private final EmplacementQueryService emplacementQueryService;

    public EmplacementResource(EmplacementService emplacementService, EmplacementQueryService emplacementQueryService) {
        this.emplacementService = emplacementService;
        this.emplacementQueryService = emplacementQueryService;
    }

    /**
     * {@code POST  /emplacements} : Create a new emplacement.
     *
     * @param emplacementDTO the emplacementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emplacementDTO, or with status {@code 400 (Bad Request)} if the emplacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emplacements")
    public ResponseEntity<EmplacementDTO> createEmplacement(@RequestBody EmplacementDTO emplacementDTO) throws URISyntaxException {
        log.debug("REST request to save Emplacement : {}", emplacementDTO);
        if (emplacementDTO.getId() != null) {
            throw new BadRequestAlertException("A new emplacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmplacementDTO result = emplacementService.save(emplacementDTO);
        return ResponseEntity.created(new URI("/api/emplacements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emplacements} : Updates an existing emplacement.
     *
     * @param emplacementDTO the emplacementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emplacementDTO,
     * or with status {@code 400 (Bad Request)} if the emplacementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emplacementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emplacements")
    public ResponseEntity<EmplacementDTO> updateEmplacement(@RequestBody EmplacementDTO emplacementDTO) throws URISyntaxException {
        log.debug("REST request to update Emplacement : {}", emplacementDTO);
        if (emplacementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmplacementDTO result = emplacementService.save(emplacementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emplacementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /emplacements} : get all the emplacements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emplacements in body.
     */
    @GetMapping("/emplacements")
    public ResponseEntity<List<EmplacementDTO>> getAllEmplacements(EmplacementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Emplacements by criteria: {}", criteria);
        Page<EmplacementDTO> page = emplacementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emplacements/count} : count all the emplacements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/emplacements/count")
    public ResponseEntity<Long> countEmplacements(EmplacementCriteria criteria) {
        log.debug("REST request to count Emplacements by criteria: {}", criteria);
        return ResponseEntity.ok().body(emplacementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /emplacements/:id} : get the "id" emplacement.
     *
     * @param id the id of the emplacementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emplacementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emplacements/{id}")
    public ResponseEntity<EmplacementDTO> getEmplacement(@PathVariable Long id) {
        log.debug("REST request to get Emplacement : {}", id);
        Optional<EmplacementDTO> emplacementDTO = emplacementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emplacementDTO);
    }

    /**
     * {@code DELETE  /emplacements/:id} : delete the "id" emplacement.
     *
     * @param id the id of the emplacementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emplacements/{id}")
    public ResponseEntity<Void> deleteEmplacement(@PathVariable Long id) {
        log.debug("REST request to delete Emplacement : {}", id);
        emplacementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
