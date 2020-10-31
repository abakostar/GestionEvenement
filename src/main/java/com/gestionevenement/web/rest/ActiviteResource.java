package com.gestionevenement.web.rest;

import com.gestionevenement.service.ActiviteQueryService;
import com.gestionevenement.service.ActiviteService;
import com.gestionevenement.service.dto.ActiviteCriteria;
import com.gestionevenement.service.dto.ActiviteDTO;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gestionevenement.domain.Activite}.
 */
@RestController
@RequestMapping("/api")
public class ActiviteResource {

    private final Logger log = LoggerFactory.getLogger(ActiviteResource.class);

    private static final String ENTITY_NAME = "activite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActiviteService activiteService;

    private final ActiviteQueryService activiteQueryService;

    public ActiviteResource(ActiviteService activiteService, ActiviteQueryService activiteQueryService) {
        this.activiteService = activiteService;
        this.activiteQueryService = activiteQueryService;
    }

    /**
     * {@code POST  /activites} : Create a new activite.
     *
     * @param activiteDTO the activiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activiteDTO, or with status {@code 400 (Bad Request)} if the activite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activites")
    public ResponseEntity<ActiviteDTO> createActivite(@RequestBody ActiviteDTO activiteDTO) throws URISyntaxException {
        log.debug("REST request to save Activite : {}", activiteDTO);
        if (activiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new activite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActiviteDTO result = activiteService.save(activiteDTO);
        return ResponseEntity.created(new URI("/api/activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activites} : Updates an existing activite.
     *
     * @param activiteDTO the activiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activiteDTO,
     * or with status {@code 400 (Bad Request)} if the activiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activites")
    public ResponseEntity<ActiviteDTO> updateActivite(@RequestBody ActiviteDTO activiteDTO) throws URISyntaxException {
        log.debug("REST request to update Activite : {}", activiteDTO);
        if (activiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActiviteDTO result = activiteService.save(activiteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activites} : get all the activites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activites in body.
     */
    @GetMapping("/activites")
    public ResponseEntity<List<ActiviteDTO>> getAllActivites(ActiviteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Activites by criteria: {}", criteria);
        Page<ActiviteDTO> page = activiteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    // pour l'api de tester de disponibilité de place, Ajout AS
    @GetMapping("/activites/placedispo")
    public Boolean testDispoplace(Long id) {
        log.debug("Teste de disponibilté de Place",id);
        Boolean val = activiteService.placedispo(id);
        return  val;
    }


    /**
     * {@code GET  /activites/count} : count all the activites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/activites/count")
    public ResponseEntity<Long> countActivites(ActiviteCriteria criteria) {
        log.debug("REST request to count Activites by criteria: {}", criteria);
        return ResponseEntity.ok().body(activiteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /activites/:id} : get the "id" activite.
     *
     * @param id the id of the activiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activites/{id}")
    public ResponseEntity<ActiviteDTO> getActivite(@PathVariable Long id) {
        log.debug("REST request to get Activite : {}", id);
        Optional<ActiviteDTO> activiteDTO = activiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activiteDTO);
    }

    /**
     * {@code DELETE  /activites/:id} : delete the "id" activite.
     *
     * @param id the id of the activiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activites/{id}")
    public ResponseEntity<Void> deleteActivite(@PathVariable Long id) {
        log.debug("REST request to delete Activite : {}", id);
        activiteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


}
