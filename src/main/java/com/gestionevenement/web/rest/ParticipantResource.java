package com.gestionevenement.web.rest;

import com.gestionevenement.domain.*;
import com.gestionevenement.repository.ParticipantActiviteRepository;
import com.gestionevenement.repository.ParticipantEvenementRepository;
import com.gestionevenement.repository.ParticipantRepository;
import com.gestionevenement.security.AuthoritiesConstants;
import com.gestionevenement.security.SecurityUtils;
import com.gestionevenement.service.ParticipantQueryService;
import com.gestionevenement.service.ParticipantService;
import com.gestionevenement.service.UserService;
import com.gestionevenement.service.dto.ParticipantActiviteDTO;
import com.gestionevenement.service.dto.ParticipantCriteria;
import com.gestionevenement.service.dto.ParticipantDTO;
import com.gestionevenement.web.rest.errors.BadRequestAlertException;
import com.gestionevenement.web.rest.vm.ManagedUserVM;
import com.gestionevenement.web.rest.vm.ParticipantActiviteIdVM;
import com.gestionevenement.web.rest.vm.ParticipantEvenementIdVM;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.gestionevenement.domain.Participant}.
 */
@RestController
@RequestMapping("/api")
public class ParticipantResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantResource.class);

    private static final String ENTITY_NAME = "participant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipantService participantService;

    private final ParticipantQueryService participantQueryService;

    private final UserService userService;

    private final ParticipantEvenementRepository participantEvenementRepository;

    private final ParticipantActiviteRepository participantActiviteRepository;

    private final ParticipantRepository participantRepository;

    public ParticipantResource(ParticipantService participantService,
                               UserService userService,
                               ParticipantQueryService participantQueryService,
                               ParticipantActiviteRepository participantActiviteRepository,
                               ParticipantRepository participantRepository,
                               ParticipantEvenementRepository participantEvenementRepository) {
        this.participantService = participantService;
        this.userService = userService;
        this.participantQueryService = participantQueryService;
        this.participantEvenementRepository = participantEvenementRepository;
        this.participantActiviteRepository = participantActiviteRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * {@code POST  /participants} : Create a new participant.
     *
     * @param participantDTO the participantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participantDTO, or with status {@code 400 (Bad Request)} if the participant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participants")
    public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody ParticipantDTO participantDTO) throws URISyntaxException {
        log.debug("REST request to save Participant : {}", participantDTO);
        if (participantDTO.getId() != null) {
            throw new BadRequestAlertException("A new participant cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ParticipantDTO result = participantService.save(participantDTO);

        // create user
        ManagedUserVM user = getManagedUserVM(participantDTO);
        userService.registerUser(user, Collections.singleton(AuthoritiesConstants.PARTICIPANT), user.getPassword(), true);
        return ResponseEntity.created(new URI("/api/participants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private ManagedUserVM getManagedUserVM(ParticipantDTO participantDTO) {
        ManagedUserVM user = new ManagedUserVM();
        user.setLogin(participantDTO.getLogin());
        user.setFirstName(participantDTO.getFirstName());
        user.setLastName(participantDTO.getLastName());
        user.setEmail(participantDTO.getEmail());
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("fr");
        user.setPassword(participantDTO.getPassword());
        user.setActivated(true);
        return user;
    }

    /**
     * {@code PUT  /participants} : Updates an existing participant.
     *
     * @param participantDTO the participantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participantDTO,
     * or with status {@code 400 (Bad Request)} if the participantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participants")
    public ResponseEntity<ParticipantDTO> updateParticipant(@RequestBody ParticipantDTO participantDTO) throws URISyntaxException {
        log.debug("REST request to update Participant : {}", participantDTO);
        if (participantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticipantDTO result = participantService.save(participantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, participantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /participants} : get all the participants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participants in body.
     */
    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(ParticipantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Participants by criteria: {}", criteria);
        StringFilter loginFilter = new StringFilter();
        loginFilter.setEquals(SecurityUtils.getCurrentUserLogin().orElse(null));
        criteria.setLogin(loginFilter);
        Page<ParticipantDTO> page = participantQueryService.findByCriteria(criteria, pageable);
        if (page.isEmpty()) {
            User user = userService.findByLogin(loginFilter.getEquals());
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setUser(user);
            page = new PageImpl<>(Collections.singletonList(participantDTO), pageable, 1);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /participants/count} : count all the participants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/participants/count")
    public ResponseEntity<Long> countParticipants(ParticipantCriteria criteria) {
        log.debug("REST request to count Participants by criteria: {}", criteria);
        return ResponseEntity.ok().body(participantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /participants/:id} : get the "id" participant.
     *
     * @param id the id of the participantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participants/{id}")
    public ResponseEntity<ParticipantDTO> getParticipant(@PathVariable Long id) {
        log.debug("REST request to get Participant : {}", id);
        Optional<ParticipantDTO> participantDTO;
        if (id == 0) {
            participantDTO = Optional.of(new ParticipantDTO());
        } else {
            participantDTO = participantService.findOne(id);
        }
        participantDTO.ifPresent(participantDTO1 -> {
            final User user = userService.findByLogin(SecurityUtils.getCurrentUserLogin().orElse(null));
            if (user != null) participantDTO1.setUser(user);
        });
        return ResponseUtil.wrapOrNotFound(participantDTO);
    }

    /**
     * {@code DELETE  /participants/:id} : delete the "id" participant.
     *
     * @param id the id of the participantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participants/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        log.debug("REST request to delete Participant : {}", id);
        Optional<ParticipantDTO> result = participantService.findOne(id);
        result.ifPresent(participantDTO -> {
            userService.deleteUser(participantDTO.getLogin());
            participantService.delete(id);
            // delete evements
            // delete activite
        });
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/participants/currentUser")
    public ResponseEntity<ParticipantDTO> getParticipantByCurrentUser() {
        final User user = userService.findByLogin(SecurityUtils.getCurrentUserLogin().orElse(null));
        log.debug("REST request to get Participant : {}", user.getLogin());
        Optional<ParticipantDTO> result = participantService.findByUser(user);
        return ResponseUtil.wrapOrNotFound(result);
    }

    @GetMapping("/participants/activite/{evenementId}")
    public ResponseEntity<List<ParticipantActiviteDTO>> getParticipantActiviteByCurrentUser(@PathVariable Long evenementId) {
        final User user = userService.findByLogin(SecurityUtils.getCurrentUserLogin().orElse(null));
        log.debug("REST request to get Participant : {}", user == null ? null : user.getLogin());
        Optional<List<ParticipantActiviteDTO>> result = participantService.findAllParticipantActiviteByUser(user, evenementId);
        return ResponseUtil.wrapOrNotFound(result);
    }

    @PostMapping("/participants/addEvenement")
    public ResponseEntity<ParticipantEvenementIdVM> addEvenement(@Valid @RequestBody ParticipantEvenementIdVM participantEvenementIdVM) throws URISyntaxException {
        log.debug("REST request to save Evenement Participant : {}", participantEvenementIdVM);
        Participant participant = participantRepository.findOneByLoginWithEagerRelationships(SecurityUtils.getCurrentUserLogin().orElse(null)).orElse(null);
        if (participant == null || !participant.getId().equals(participantEvenementIdVM.getParticipantId())) {
            throw new BadRequestAlertException("Invalid participant connected", ENTITY_NAME, participantEvenementIdVM.getParticipantId() == null ? "nullId" : participantEvenementIdVM.getParticipantId().toString());
        }
        if (participantEvenementIdVM.getEvenementId() == null || participantEvenementIdVM.getParticipantId() == null) {
            throw new BadRequestAlertException("A new evenement cannot already have an participantId or evenementId", "ParticipantEvenementId", "idexists");
        }
        ParticipantEvenemrntId participantEvenemrntId = new ParticipantEvenemrntId(participantEvenementIdVM.getParticipantId(), participantEvenementIdVM.getEvenementId());
        Optional<ParticipantEvenement> evenementRepositoryById = participantEvenementRepository.findById(participantEvenemrntId);
        if (participantEvenementIdVM.isRegistered() && !evenementRepositoryById.isPresent()) {
            ParticipantEvenement participantEvenement = new ParticipantEvenement();
            participantEvenement.setEvenementId(participantEvenementIdVM.getEvenementId());
            participantEvenement.setParticipantId(participantEvenementIdVM.getParticipantId());
            participantEvenementRepository.save(participantEvenement);
        } else if (!participantEvenementIdVM.isRegistered() && evenementRepositoryById.isPresent()) {
            participantEvenementRepository.deleteById(participantEvenemrntId);
        }
        return ResponseEntity.created(new URI("/api/participants/addParticipant/" + participantEvenementIdVM.getEvenementId() + "/" + participantEvenementIdVM.getParticipantId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, participantEvenementIdVM.getEvenementId().toString() + "_" + participantEvenementIdVM.getParticipantId()))
            .body(participantEvenementIdVM);
    }

    @PostMapping("/participants/addActivite")
    public ResponseEntity<ParticipantActiviteIdVM> addActivite(@Valid @RequestBody ParticipantActiviteIdVM participantActiviteIdVM) throws URISyntaxException {
        log.debug("REST request to save Activite Participant : {}", participantActiviteIdVM);
        Participant participant = participantRepository.findOneByLoginWithEagerRelationships(SecurityUtils.getCurrentUserLogin().orElse(null)).orElse(null);
        if (participant == null || !participant.getId().equals(participantActiviteIdVM.getParticipantId())) {
            throw new BadRequestAlertException("Invalid participant connected", ENTITY_NAME, participantActiviteIdVM.getParticipantId() == null ? "nullId" : participantActiviteIdVM.getParticipantId().toString());
        }
        if (participantActiviteIdVM.getActiviteId() == null || participantActiviteIdVM.getParticipantId() == null) {
            throw new BadRequestAlertException("A new evenement cannot already have an participantId or evenementId", "ParticipantEvenementId", "idexists");
        }
        ParticipantActiviteId participantActiviteId = new ParticipantActiviteId(participantActiviteIdVM.getParticipantId(), participantActiviteIdVM.getActiviteId());
        Optional<ParticipantActivite> activiteRepositoryById = participantActiviteRepository.findById(participantActiviteId);
        if (participantActiviteIdVM.isRegistered() && !activiteRepositoryById.isPresent()) {
            ParticipantActivite participantActivite = new ParticipantActivite();
            participantActivite.setActiviteId(participantActiviteIdVM.getActiviteId());
            participantActivite.setParticipantId(participantActiviteIdVM.getParticipantId());
            participantActivite.setRole(participantActiviteIdVM.getRole());
            participantActiviteRepository.save(participantActivite);
        } else if (!participantActiviteIdVM.isRegistered() && activiteRepositoryById.isPresent()) {
            participantActiviteRepository.deleteById(participantActiviteId);
        }
        return ResponseEntity.created(new URI("/api/participants/addActivite/" + participantActiviteIdVM.getActiviteId() + "/" + participantActiviteIdVM.getParticipantId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, participantActiviteIdVM.getActiviteId().toString() + "_" + participantActiviteIdVM.getParticipantId()))
            .body(participantActiviteIdVM);
    }
}
