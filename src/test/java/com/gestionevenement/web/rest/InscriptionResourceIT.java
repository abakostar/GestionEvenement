package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Inscription;
import com.gestionevenement.domain.Activite;
import com.gestionevenement.domain.Participant;
import com.gestionevenement.repository.InscriptionRepository;
import com.gestionevenement.service.InscriptionService;
import com.gestionevenement.service.dto.InscriptionDTO;
import com.gestionevenement.service.mapper.InscriptionMapper;
import com.gestionevenement.service.dto.InscriptionCriteria;
import com.gestionevenement.service.InscriptionQueryService;

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
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InscriptionResourceIT {

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private InscriptionQueryService inscriptionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .role(DEFAULT_ROLE);
        return inscription;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .role(UPDATED_ROLE);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);
        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createInscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // Create the Inscription with an existing ID
        inscription.setId(1L);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc.perform(post("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }
    
    @Test
    @Transactional
    public void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }


    @Test
    @Transactional
    public void getInscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        Long id = inscription.getId();

        defaultInscriptionShouldBeFound("id.equals=" + id);
        defaultInscriptionShouldNotBeFound("id.notEquals=" + id);

        defaultInscriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInscriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultInscriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInscriptionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role equals to DEFAULT_ROLE
        defaultInscriptionShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the inscriptionList where role equals to UPDATED_ROLE
        defaultInscriptionShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role not equals to DEFAULT_ROLE
        defaultInscriptionShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the inscriptionList where role not equals to UPDATED_ROLE
        defaultInscriptionShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultInscriptionShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the inscriptionList where role equals to UPDATED_ROLE
        defaultInscriptionShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role is not null
        defaultInscriptionShouldBeFound("role.specified=true");

        // Get all the inscriptionList where role is null
        defaultInscriptionShouldNotBeFound("role.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionsByRoleContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role contains DEFAULT_ROLE
        defaultInscriptionShouldBeFound("role.contains=" + DEFAULT_ROLE);

        // Get all the inscriptionList where role contains UPDATED_ROLE
        defaultInscriptionShouldNotBeFound("role.contains=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionsByRoleNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList where role does not contain DEFAULT_ROLE
        defaultInscriptionShouldNotBeFound("role.doesNotContain=" + DEFAULT_ROLE);

        // Get all the inscriptionList where role does not contain UPDATED_ROLE
        defaultInscriptionShouldBeFound("role.doesNotContain=" + UPDATED_ROLE);
    }


    @Test
    @Transactional
    public void getAllInscriptionsByActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        Activite activite = ActiviteResourceIT.createEntity(em);
        em.persist(activite);
        em.flush();
        inscription.setActivite(activite);
        inscriptionRepository.saveAndFlush(inscription);
        Long activiteId = activite.getId();

        // Get all the inscriptionList where activite equals to activiteId
        defaultInscriptionShouldBeFound("activiteId.equals=" + activiteId);

        // Get all the inscriptionList where activite equals to activiteId + 1
        defaultInscriptionShouldNotBeFound("activiteId.equals=" + (activiteId + 1));
    }


    @Test
    @Transactional
    public void getAllInscriptionsByParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        Participant participant = ParticipantResourceIT.createEntity(em);
        em.persist(participant);
        em.flush();
        inscription.setParticipant(participant);
        inscriptionRepository.saveAndFlush(inscription);
        Long participantId = participant.getId();

        // Get all the inscriptionList where participant equals to participantId
        defaultInscriptionShouldBeFound("participantId.equals=" + participantId);

        // Get all the inscriptionList where participant equals to participantId + 1
        defaultInscriptionShouldNotBeFound("participantId.equals=" + (participantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscriptionShouldBeFound(String filter) throws Exception {
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));

        // Check, that the count call also returns 1
        restInscriptionMockMvc.perform(get("/api/inscriptions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscriptionShouldNotBeFound(String filter) throws Exception {
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscriptionMockMvc.perform(get("/api/inscriptions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription
            .role(UPDATED_ROLE);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc.perform(put("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc.perform(put("/api/inscriptions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc.perform(delete("/api/inscriptions/{id}", inscription.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
