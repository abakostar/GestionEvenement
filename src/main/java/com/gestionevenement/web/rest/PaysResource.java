package com.gestionevenement.web.rest;

import com.gestionevenement.service.PaysService;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.service.dto.PaysDTO;
import com.gestionevenement.service.dto.PaysCriteria;
import com.gestionevenement.service.PaysQueryService;

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
 * REST controller for managing {@link com.gestionevenement.domain.Pays}.
 */
@RestController
@RequestMapping("/api")
public class PaysResource {

    private final Logger log = LoggerFactory.getLogger(PaysResource.class);

    private static final String ENTITY_NAME = "pays";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaysService paysService;

    private final PaysQueryService paysQueryService;

    public PaysResource(PaysService paysService, PaysQueryService paysQueryService) {
        this.paysService = paysService;
        this.paysQueryService = paysQueryService;
    }

    /**
     * {@code POST  /pays} : Create a new pays.
     *
     * @param paysDTO the paysDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paysDTO, or with status {@code 400 (Bad Request)} if the pays has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pays")
    public ResponseEntity<PaysDTO> createPays(@RequestBody PaysDTO paysDTO) throws URISyntaxException {
        log.debug("REST request to save Pays : {}", paysDTO);
        if (paysDTO.getId() != null) {
            throw new BadRequestAlertException("A new pays cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaysDTO result = paysService.save(paysDTO);
        return ResponseEntity.created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pays} : Updates an existing pays.
     *
     * @param paysDTO the paysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paysDTO,
     * or with status {@code 400 (Bad Request)} if the paysDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pays")
    public ResponseEntity<PaysDTO> updatePays(@RequestBody PaysDTO paysDTO) throws URISyntaxException {
        log.debug("REST request to update Pays : {}", paysDTO);
        if (paysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaysDTO result = paysService.save(paysDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paysDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pays} : get all the pays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pays in body.
     */
    @GetMapping("/pays")
    public ResponseEntity<List<PaysDTO>> getAllPays(PaysCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pays by criteria: {}", criteria);
        Page<PaysDTO> page = paysQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pays/count} : count all the pays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pays/count")
    public ResponseEntity<Long> countPays(PaysCriteria criteria) {
        log.debug("REST request to count Pays by criteria: {}", criteria);
        return ResponseEntity.ok().body(paysQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pays/:id} : get the "id" pays.
     *
     * @param id the id of the paysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pays/{id}")
    public ResponseEntity<PaysDTO> getPays(@PathVariable Long id) {
        log.debug("REST request to get Pays : {}", id);
        Optional<PaysDTO> paysDTO = paysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paysDTO);
    }

    /**
     * {@code DELETE  /pays/:id} : delete the "id" pays.
     *
     * @param id the id of the paysDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Void> deletePays(@PathVariable Long id) {
        log.debug("REST request to delete Pays : {}", id);
        paysService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
