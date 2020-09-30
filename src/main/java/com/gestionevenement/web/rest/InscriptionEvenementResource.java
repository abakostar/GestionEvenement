package com.gestionevenement.web.rest;

import com.gestionevenement.service.InscriptionEvenementService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.InscriptionEvenementDTO;
import com.gestionevenement.service.dto.InscriptionEvenementCriteria;
import com.gestionevenement.service.InscriptionEvenementQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gestionevenement.domain.InscriptionEvenement}.
 */
@RestController
@RequestMapping("/api")
public class InscriptionEvenementResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionEvenementResource.class);

    private static final String ENTITY_NAME = "inscriptionEvenement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionEvenementService inscriptionEvenementService;

    private final InscriptionEvenementQueryService inscriptionEvenementQueryService;

    public InscriptionEvenementResource(InscriptionEvenementService inscriptionEvenementService, InscriptionEvenementQueryService inscriptionEvenementQueryService) {
        this.inscriptionEvenementService = inscriptionEvenementService;
        this.inscriptionEvenementQueryService = inscriptionEvenementQueryService;
    }

    /**
     * {@code POST  /inscription-evenements} : Create a new inscriptionEvenement.
     *
     * @param inscriptionEvenementDTO the inscriptionEvenementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionEvenementDTO, or with status {@code 400 (Bad Request)} if the inscriptionEvenement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscription-evenements")
    public ResponseEntity<InscriptionEvenementDTO> createInscriptionEvenement(@Valid @RequestBody InscriptionEvenementDTO inscriptionEvenementDTO) throws URISyntaxException {
        log.debug("REST request to save InscriptionEvenement : {}", inscriptionEvenementDTO);
        if (inscriptionEvenementDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscriptionEvenement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionEvenementDTO result = inscriptionEvenementService.save(inscriptionEvenementDTO);
        return ResponseEntity.created(new URI("/api/inscription-evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscription-evenements} : Updates an existing inscriptionEvenement.
     *
     * @param inscriptionEvenementDTO the inscriptionEvenementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionEvenementDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionEvenementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionEvenementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscription-evenements")
    public ResponseEntity<InscriptionEvenementDTO> updateInscriptionEvenement(@Valid @RequestBody InscriptionEvenementDTO inscriptionEvenementDTO) throws URISyntaxException {
        log.debug("REST request to update InscriptionEvenement : {}", inscriptionEvenementDTO);
        if (inscriptionEvenementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InscriptionEvenementDTO result = inscriptionEvenementService.save(inscriptionEvenementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionEvenementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inscription-evenements} : get all the inscriptionEvenements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionEvenements in body.
     */
    @GetMapping("/inscription-evenements")
    public ResponseEntity<List<InscriptionEvenementDTO>> getAllInscriptionEvenements(InscriptionEvenementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InscriptionEvenements by criteria: {}", criteria);
        Page<InscriptionEvenementDTO> page = inscriptionEvenementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-evenements/count} : count all the inscriptionEvenements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inscription-evenements/count")
    public ResponseEntity<Long> countInscriptionEvenements(InscriptionEvenementCriteria criteria) {
        log.debug("REST request to count InscriptionEvenements by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscriptionEvenementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inscription-evenements/:id} : get the "id" inscriptionEvenement.
     *
     * @param id the id of the inscriptionEvenementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionEvenementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscription-evenements/{id}")
    public ResponseEntity<InscriptionEvenementDTO> getInscriptionEvenement(@PathVariable Long id) {
        log.debug("REST request to get InscriptionEvenement : {}", id);
        Optional<InscriptionEvenementDTO> inscriptionEvenementDTO = inscriptionEvenementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionEvenementDTO);
    }

    /**
     * {@code DELETE  /inscription-evenements/:id} : delete the "id" inscriptionEvenement.
     *
     * @param id the id of the inscriptionEvenementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscription-evenements/{id}")
    public ResponseEntity<Void> deleteInscriptionEvenement(@PathVariable Long id) {
        log.debug("REST request to delete InscriptionEvenement : {}", id);
        inscriptionEvenementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
