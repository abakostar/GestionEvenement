package com.gestionevenement.web.rest;

import com.gestionevenement.service.VilleService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.VilleDTO;
import com.gestionevenement.service.dto.VilleCriteria;
import com.gestionevenement.service.VilleQueryService;

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
 * REST controller for managing {@link com.gestionevenement.domain.Ville}.
 */
@RestController
@RequestMapping("/api")
public class VilleResource {

    private final Logger log = LoggerFactory.getLogger(VilleResource.class);

    private static final String ENTITY_NAME = "ville";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VilleService villeService;

    private final VilleQueryService villeQueryService;

    public VilleResource(VilleService villeService, VilleQueryService villeQueryService) {
        this.villeService = villeService;
        this.villeQueryService = villeQueryService;
    }

    /**
     * {@code POST  /villes} : Create a new ville.
     *
     * @param villeDTO the villeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new villeDTO, or with status {@code 400 (Bad Request)} if the ville has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/villes")
    public ResponseEntity<VilleDTO> createVille(@RequestBody VilleDTO villeDTO) throws URISyntaxException {
        log.debug("REST request to save Ville : {}", villeDTO);
        if (villeDTO.getId() != null) {
            throw new BadRequestAlertException("A new ville cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VilleDTO result = villeService.save(villeDTO);
        return ResponseEntity.created(new URI("/api/villes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /villes} : Updates an existing ville.
     *
     * @param villeDTO the villeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated villeDTO,
     * or with status {@code 400 (Bad Request)} if the villeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the villeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/villes")
    public ResponseEntity<VilleDTO> updateVille(@RequestBody VilleDTO villeDTO) throws URISyntaxException {
        log.debug("REST request to update Ville : {}", villeDTO);
        if (villeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VilleDTO result = villeService.save(villeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, villeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /villes} : get all the villes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of villes in body.
     */
    @GetMapping("/villes")
    public ResponseEntity<List<VilleDTO>> getAllVilles(VilleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Villes by criteria: {}", criteria);
        Page<VilleDTO> page = villeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /villes/count} : count all the villes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/villes/count")
    public ResponseEntity<Long> countVilles(VilleCriteria criteria) {
        log.debug("REST request to count Villes by criteria: {}", criteria);
        return ResponseEntity.ok().body(villeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /villes/:id} : get the "id" ville.
     *
     * @param id the id of the villeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the villeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/villes/{id}")
    public ResponseEntity<VilleDTO> getVille(@PathVariable Long id) {
        log.debug("REST request to get Ville : {}", id);
        Optional<VilleDTO> villeDTO = villeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(villeDTO);
    }

    /**
     * {@code DELETE  /villes/:id} : delete the "id" ville.
     *
     * @param id the id of the villeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/villes/{id}")
    public ResponseEntity<Void> deleteVille(@PathVariable Long id) {
        log.debug("REST request to delete Ville : {}", id);
        villeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
