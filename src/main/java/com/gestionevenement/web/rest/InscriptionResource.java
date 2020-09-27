package com.gestionevenement.web.rest;

import com.gestionevenement.service.InscriptionService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.InscriptionDTO;
import com.gestionevenement.service.dto.InscriptionCriteria;
import com.gestionevenement.service.InscriptionQueryService;

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
 * REST controller for managing {@link com.gestionevenement.domain.Inscription}.
 */
@RestController
@RequestMapping("/api")
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "inscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionService inscriptionService;

    private final InscriptionQueryService inscriptionQueryService;

    public InscriptionResource(InscriptionService inscriptionService, InscriptionQueryService inscriptionQueryService) {
        this.inscriptionService = inscriptionService;
        this.inscriptionQueryService = inscriptionQueryService;
    }

    /**
     * {@code POST  /inscriptions} : Create a new inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionDTO, or with status {@code 400 (Bad Request)} if the inscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inscriptions")
    public ResponseEntity<InscriptionDTO> createInscription(@RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.created(new URI("/api/inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inscriptions} : Updates an existing inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inscriptions")
    public ResponseEntity<InscriptionDTO> updateInscription(@RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /inscriptions} : get all the inscriptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptions in body.
     */
    @GetMapping("/inscriptions")
    public ResponseEntity<List<InscriptionDTO>> getAllInscriptions(InscriptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Inscriptions by criteria: {}", criteria);
        Page<InscriptionDTO> page = inscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscriptions/count} : count all the inscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/inscriptions/count")
    public ResponseEntity<Long> countInscriptions(InscriptionCriteria criteria) {
        log.debug("REST request to count Inscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inscriptions/:id} : get the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inscriptions/{id}")
    public ResponseEntity<InscriptionDTO> getInscription(@PathVariable Long id) {
        log.debug("REST request to get Inscription : {}", id);
        Optional<InscriptionDTO> inscriptionDTO = inscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionDTO);
    }

    /**
     * {@code DELETE  /inscriptions/:id} : delete the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inscriptions/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
