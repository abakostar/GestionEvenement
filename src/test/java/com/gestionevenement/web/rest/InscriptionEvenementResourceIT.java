package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.InscriptionEvenement;
import com.gestionevenement.domain.Evenement;
import com.gestionevenement.domain.Participant;
import com.gestionevenement.repository.InscriptionEvenementRepository;
import com.gestionevenement.service.InscriptionEvenementService;
import com.gestionevenement.service.dto.InscriptionEvenementDTO;
import com.gestionevenement.service.mapper.InscriptionEvenementMapper;
import com.gestionevenement.service.dto.InscriptionEvenementCriteria;
import com.gestionevenement.service.InscriptionEvenementQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InscriptionEvenementResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InscriptionEvenementResourceIT {

    private static final String DEFAULT_LOGIN_PARTICIPANT = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_PARTICIPANT = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_PARTICIPANT = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_PARTICIPANT = "BBBBBBBBBB";

    @Autowired
    private InscriptionEvenementRepository inscriptionEvenementRepository;

    @Autowired
    private InscriptionEvenementMapper inscriptionEvenementMapper;

    @Autowired
    private InscriptionEvenementService inscriptionEvenementService;

    @Autowired
    private InscriptionEvenementQueryService inscriptionEvenementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionEvenementMockMvc;

    private InscriptionEvenement inscriptionEvenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionEvenement createEntity(EntityManager em) {
        InscriptionEvenement inscriptionEvenement = new InscriptionEvenement()
            .loginParticipant(DEFAULT_LOGIN_PARTICIPANT)
            .passwordParticipant(DEFAULT_PASSWORD_PARTICIPANT);
        // Add required entity
        Evenement evenement;
        if (TestUtil.findAll(em, Evenement.class).isEmpty()) {
            evenement = EvenementResourceIT.createEntity(em);
            em.persist(evenement);
            em.flush();
        } else {
            evenement = TestUtil.findAll(em, Evenement.class).get(0);
        }
        inscriptionEvenement.setEvenement(evenement);
        return inscriptionEvenement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionEvenement createUpdatedEntity(EntityManager em) {
        InscriptionEvenement inscriptionEvenement = new InscriptionEvenement()
            .loginParticipant(UPDATED_LOGIN_PARTICIPANT)
            .passwordParticipant(UPDATED_PASSWORD_PARTICIPANT);
        // Add required entity
        Evenement evenement;
        if (TestUtil.findAll(em, Evenement.class).isEmpty()) {
            evenement = EvenementResourceIT.createUpdatedEntity(em);
            em.persist(evenement);
            em.flush();
        } else {
            evenement = TestUtil.findAll(em, Evenement.class).get(0);
        }
        inscriptionEvenement.setEvenement(evenement);
        return inscriptionEvenement;
    }

    @BeforeEach
    public void initTest() {
        inscriptionEvenement = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscriptionEvenement() throws Exception {
        int databaseSizeBeforeCreate = inscriptionEvenementRepository.findAll().size();
        // Create the InscriptionEvenement
        InscriptionEvenementDTO inscriptionEvenementDTO = inscriptionEvenementMapper.toDto(inscriptionEvenement);
        restInscriptionEvenementMockMvc.perform(post("/api/inscription-evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionEvenementDTO)))
            .andExpect(status().isCreated());

        // Validate the InscriptionEvenement in the database
        List<InscriptionEvenement> inscriptionEvenementList = inscriptionEvenementRepository.findAll();
        assertThat(inscriptionEvenementList).hasSize(databaseSizeBeforeCreate + 1);
        InscriptionEvenement testInscriptionEvenement = inscriptionEvenementList.get(inscriptionEvenementList.size() - 1);
        assertThat(testInscriptionEvenement.getLoginParticipant()).isEqualTo(DEFAULT_LOGIN_PARTICIPANT);
        assertThat(testInscriptionEvenement.getPasswordParticipant()).isEqualTo(DEFAULT_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void createInscriptionEvenementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscriptionEvenementRepository.findAll().size();

        // Create the InscriptionEvenement with an existing ID
        inscriptionEvenement.setId(1L);
        InscriptionEvenementDTO inscriptionEvenementDTO = inscriptionEvenementMapper.toDto(inscriptionEvenement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionEvenementMockMvc.perform(post("/api/inscription-evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionEvenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InscriptionEvenement in the database
        List<InscriptionEvenement> inscriptionEvenementList = inscriptionEvenementRepository.findAll();
        assertThat(inscriptionEvenementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInscriptionEvenements() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginParticipant").value(hasItem(DEFAULT_LOGIN_PARTICIPANT)))
            .andExpect(jsonPath("$.[*].passwordParticipant").value(hasItem(DEFAULT_PASSWORD_PARTICIPANT)));
    }
    
    @Test
    @Transactional
    public void getInscriptionEvenement() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get the inscriptionEvenement
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements/{id}", inscriptionEvenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionEvenement.getId().intValue()))
            .andExpect(jsonPath("$.loginParticipant").value(DEFAULT_LOGIN_PARTICIPANT))
            .andExpect(jsonPath("$.passwordParticipant").value(DEFAULT_PASSWORD_PARTICIPANT));
    }


    @Test
    @Transactional
    public void getInscriptionEvenementsByIdFiltering() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        Long id = inscriptionEvenement.getId();

        defaultInscriptionEvenementShouldBeFound("id.equals=" + id);
        defaultInscriptionEvenementShouldNotBeFound("id.notEquals=" + id);

        defaultInscriptionEvenementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInscriptionEvenementShouldNotBeFound("id.greaterThan=" + id);

        defaultInscriptionEvenementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInscriptionEvenementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant equals to DEFAULT_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("loginParticipant.equals=" + DEFAULT_LOGIN_PARTICIPANT);

        // Get all the inscriptionEvenementList where loginParticipant equals to UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.equals=" + UPDATED_LOGIN_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant not equals to DEFAULT_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.notEquals=" + DEFAULT_LOGIN_PARTICIPANT);

        // Get all the inscriptionEvenementList where loginParticipant not equals to UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("loginParticipant.notEquals=" + UPDATED_LOGIN_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant in DEFAULT_LOGIN_PARTICIPANT or UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("loginParticipant.in=" + DEFAULT_LOGIN_PARTICIPANT + "," + UPDATED_LOGIN_PARTICIPANT);

        // Get all the inscriptionEvenementList where loginParticipant equals to UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.in=" + UPDATED_LOGIN_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant is not null
        defaultInscriptionEvenementShouldBeFound("loginParticipant.specified=true");

        // Get all the inscriptionEvenementList where loginParticipant is null
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantContainsSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant contains DEFAULT_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("loginParticipant.contains=" + DEFAULT_LOGIN_PARTICIPANT);

        // Get all the inscriptionEvenementList where loginParticipant contains UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.contains=" + UPDATED_LOGIN_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByLoginParticipantNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where loginParticipant does not contain DEFAULT_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("loginParticipant.doesNotContain=" + DEFAULT_LOGIN_PARTICIPANT);

        // Get all the inscriptionEvenementList where loginParticipant does not contain UPDATED_LOGIN_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("loginParticipant.doesNotContain=" + UPDATED_LOGIN_PARTICIPANT);
    }


    @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant equals to DEFAULT_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.equals=" + DEFAULT_PASSWORD_PARTICIPANT);

        // Get all the inscriptionEvenementList where passwordParticipant equals to UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.equals=" + UPDATED_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant not equals to DEFAULT_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.notEquals=" + DEFAULT_PASSWORD_PARTICIPANT);

        // Get all the inscriptionEvenementList where passwordParticipant not equals to UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.notEquals=" + UPDATED_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant in DEFAULT_PASSWORD_PARTICIPANT or UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.in=" + DEFAULT_PASSWORD_PARTICIPANT + "," + UPDATED_PASSWORD_PARTICIPANT);

        // Get all the inscriptionEvenementList where passwordParticipant equals to UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.in=" + UPDATED_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant is not null
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.specified=true");

        // Get all the inscriptionEvenementList where passwordParticipant is null
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantContainsSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant contains DEFAULT_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.contains=" + DEFAULT_PASSWORD_PARTICIPANT);

        // Get all the inscriptionEvenementList where passwordParticipant contains UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.contains=" + UPDATED_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllInscriptionEvenementsByPasswordParticipantNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        // Get all the inscriptionEvenementList where passwordParticipant does not contain DEFAULT_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldNotBeFound("passwordParticipant.doesNotContain=" + DEFAULT_PASSWORD_PARTICIPANT);

        // Get all the inscriptionEvenementList where passwordParticipant does not contain UPDATED_PASSWORD_PARTICIPANT
        defaultInscriptionEvenementShouldBeFound("passwordParticipant.doesNotContain=" + UPDATED_PASSWORD_PARTICIPANT);
    }


    @Test
    @Transactional
    public void getAllInscriptionEvenementsByEvenementIsEqualToSomething() throws Exception {
        // Get already existing entity
        Evenement evenement = inscriptionEvenement.getEvenement();
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);
        Long evenementId = evenement.getId();

        // Get all the inscriptionEvenementList where evenement equals to evenementId
        defaultInscriptionEvenementShouldBeFound("evenementId.equals=" + evenementId);

        // Get all the inscriptionEvenementList where evenement equals to evenementId + 1
        defaultInscriptionEvenementShouldNotBeFound("evenementId.equals=" + (evenementId + 1));
    }


    @Test
    @Transactional
    public void getAllInscriptionEvenementsByParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);
        Participant participant = ParticipantResourceIT.createEntity(em);
        em.persist(participant);
        em.flush();
        inscriptionEvenement.setParticipant(participant);
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);
        Long participantId = participant.getId();

        // Get all the inscriptionEvenementList where participant equals to participantId
        defaultInscriptionEvenementShouldBeFound("participantId.equals=" + participantId);

        // Get all the inscriptionEvenementList where participant equals to participantId + 1
        defaultInscriptionEvenementShouldNotBeFound("participantId.equals=" + (participantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscriptionEvenementShouldBeFound(String filter) throws Exception {
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginParticipant").value(hasItem(DEFAULT_LOGIN_PARTICIPANT)))
            .andExpect(jsonPath("$.[*].passwordParticipant").value(hasItem(DEFAULT_PASSWORD_PARTICIPANT)));

        // Check, that the count call also returns 1
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscriptionEvenementShouldNotBeFound(String filter) throws Exception {
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInscriptionEvenement() throws Exception {
        // Get the inscriptionEvenement
        restInscriptionEvenementMockMvc.perform(get("/api/inscription-evenements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscriptionEvenement() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        int databaseSizeBeforeUpdate = inscriptionEvenementRepository.findAll().size();

        // Update the inscriptionEvenement
        InscriptionEvenement updatedInscriptionEvenement = inscriptionEvenementRepository.findById(inscriptionEvenement.getId()).get();
        // Disconnect from session so that the updates on updatedInscriptionEvenement are not directly saved in db
        em.detach(updatedInscriptionEvenement);
        updatedInscriptionEvenement
            .loginParticipant(UPDATED_LOGIN_PARTICIPANT)
            .passwordParticipant(UPDATED_PASSWORD_PARTICIPANT);
        InscriptionEvenementDTO inscriptionEvenementDTO = inscriptionEvenementMapper.toDto(updatedInscriptionEvenement);

        restInscriptionEvenementMockMvc.perform(put("/api/inscription-evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionEvenementDTO)))
            .andExpect(status().isOk());

        // Validate the InscriptionEvenement in the database
        List<InscriptionEvenement> inscriptionEvenementList = inscriptionEvenementRepository.findAll();
        assertThat(inscriptionEvenementList).hasSize(databaseSizeBeforeUpdate);
        InscriptionEvenement testInscriptionEvenement = inscriptionEvenementList.get(inscriptionEvenementList.size() - 1);
        assertThat(testInscriptionEvenement.getLoginParticipant()).isEqualTo(UPDATED_LOGIN_PARTICIPANT);
        assertThat(testInscriptionEvenement.getPasswordParticipant()).isEqualTo(UPDATED_PASSWORD_PARTICIPANT);
    }

    @Test
    @Transactional
    public void updateNonExistingInscriptionEvenement() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionEvenementRepository.findAll().size();

        // Create the InscriptionEvenement
        InscriptionEvenementDTO inscriptionEvenementDTO = inscriptionEvenementMapper.toDto(inscriptionEvenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionEvenementMockMvc.perform(put("/api/inscription-evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionEvenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InscriptionEvenement in the database
        List<InscriptionEvenement> inscriptionEvenementList = inscriptionEvenementRepository.findAll();
        assertThat(inscriptionEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInscriptionEvenement() throws Exception {
        // Initialize the database
        inscriptionEvenementRepository.saveAndFlush(inscriptionEvenement);

        int databaseSizeBeforeDelete = inscriptionEvenementRepository.findAll().size();

        // Delete the inscriptionEvenement
        restInscriptionEvenementMockMvc.perform(delete("/api/inscription-evenements/{id}", inscriptionEvenement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionEvenement> inscriptionEvenementList = inscriptionEvenementRepository.findAll();
        assertThat(inscriptionEvenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
