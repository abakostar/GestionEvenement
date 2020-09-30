package com.gestionevenement.web.rest;

import com.gestionevenement.service.InscriptionActiviteService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.InscriptionActiviteDTO;
import com.gestionevenement.service.dto.InscriptionActiviteCriteria;
import com.gestionevenement.service.InscriptionActiviteQueryService;

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
 * REST controller for managing {@link com.gestionevenement.domain.InscriptionActivite}.
 */
@RestController
@RequestMapping("/api")
public class InscriptionActiviteResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionActiviteResource.class);

    private static final String ENTITY_NAME = "inscriptionActivite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionActiviteService inscriptionActiviteService;

    private final InscriptionActiviteQueryService inscriptionActiviteQueryService;

    public InscriptionActiviteResource(InscriptionActiviteService inscriptionActiviteService, InscriptionActiviteQueryService inscriptionActiviteQueryService) {
        this.inscriptionActiviteService = inscriptionActiviteService;
        this.inscriptionActiviteQueryService = inscriptionActiviteQueryService;
    }

    /**
     * {@code POST  /inscription-activites} : Create a new inscriptionActivite.
     *
     * @param inscriptionActiviteDTO the inscriptionActiviteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionActiviteDTO, or with status {@code 400 (Bad Request)} if the inscriptionActivite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscription-activites")
    public ResponseEntity<InscriptionActiviteDTO> createInscriptionActivite(@RequestBody InscriptionActiviteDTO inscriptionActiviteDTO) throws URISyntaxException {
        log.debug("REST request to save InscriptionActivite : {}", inscriptionActiviteDTO);
        if (inscriptionActiviteDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscriptionActivite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionActiviteDTO result = inscriptionActiviteService.save(inscriptionActiviteDTO);
        return ResponseEntity.created(new URI("/api/inscription-activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscription-activites} : Updates an existing inscriptionActivite.
     *
     * @param inscriptionActiviteDTO the inscriptionActiviteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionActiviteDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionActiviteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionActiviteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscription-activites")
    public ResponseEntity<InscriptionActiviteDTO> updateInscriptionActivite(@RequestBody InscriptionActiviteDTO inscriptionActiviteDTO) throws URISyntaxException {
        log.debug("REST request to update InscriptionActivite : {}", inscriptionActiviteDTO);
        if (inscriptionActiviteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InscriptionActiviteDTO result = inscriptionActiviteService.save(inscriptionActiviteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionActiviteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inscription-activites} : get all the inscriptionActivites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionActivites in body.
     */
    @GetMapping("/inscription-activites")
    public ResponseEntity<List<InscriptionActiviteDTO>> getAllInscriptionActivites(InscriptionActiviteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InscriptionActivites by criteria: {}", criteria);
        Page<InscriptionActiviteDTO> page = inscriptionActiviteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-activites/count} : count all the inscriptionActivites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inscription-activites/count")
    public ResponseEntity<Long> countInscriptionActivites(InscriptionActiviteCriteria criteria) {
        log.debug("REST request to count InscriptionActivites by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscriptionActiviteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inscription-activites/:id} : get the "id" inscriptionActivite.
     *
     * @param id the id of the inscriptionActiviteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionActiviteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscription-activites/{id}")
    public ResponseEntity<InscriptionActiviteDTO> getInscriptionActivite(@PathVariable Long id) {
        log.debug("REST request to get InscriptionActivite : {}", id);
        Optional<InscriptionActiviteDTO> inscriptionActiviteDTO = inscriptionActiviteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionActiviteDTO);
    }

    /**
     * {@code DELETE  /inscription-activites/:id} : delete the "id" inscriptionActivite.
     *
     * @param id the id of the inscriptionActiviteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscription-activites/{id}")
    public ResponseEntity<Void> deleteInscriptionActivite(@PathVariable Long id) {
        log.debug("REST request to delete InscriptionActivite : {}", id);
        inscriptionActiviteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
