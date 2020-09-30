package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.InscriptionActivite;
import com.gestionevenement.domain.Activite;
import com.gestionevenement.domain.Participant;
import com.gestionevenement.repository.InscriptionActiviteRepository;
import com.gestionevenement.service.InscriptionActiviteService;
import com.gestionevenement.service.dto.InscriptionActiviteDTO;
import com.gestionevenement.service.mapper.InscriptionActiviteMapper;
import com.gestionevenement.service.dto.InscriptionActiviteCriteria;
import com.gestionevenement.service.InscriptionActiviteQueryService;

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
 * Integration tests for the {@link InscriptionActiviteResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InscriptionActiviteResourceIT {

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    @Autowired
    private InscriptionActiviteRepository inscriptionActiviteRepository;

    @Autowired
    private InscriptionActiviteMapper inscriptionActiviteMapper;

    @Autowired
    private InscriptionActiviteService inscriptionActiviteService;

    @Autowired
    private InscriptionActiviteQueryService inscriptionActiviteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionActiviteMockMvc;

    private InscriptionActivite inscriptionActivite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionActivite createEntity(EntityManager em) {
        InscriptionActivite inscriptionActivite = new InscriptionActivite()
            .role(DEFAULT_ROLE);
        return inscriptionActivite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionActivite createUpdatedEntity(EntityManager em) {
        InscriptionActivite inscriptionActivite = new InscriptionActivite()
            .role(UPDATED_ROLE);
        return inscriptionActivite;
    }

    @BeforeEach
    public void initTest() {
        inscriptionActivite = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscriptionActivite() throws Exception {
        int databaseSizeBeforeCreate = inscriptionActiviteRepository.findAll().size();
        // Create the InscriptionActivite
        InscriptionActiviteDTO inscriptionActiviteDTO = inscriptionActiviteMapper.toDto(inscriptionActivite);
        restInscriptionActiviteMockMvc.perform(post("/api/inscription-activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionActiviteDTO)))
            .andExpect(status().isCreated());

        // Validate the InscriptionActivite in the database
        List<InscriptionActivite> inscriptionActiviteList = inscriptionActiviteRepository.findAll();
        assertThat(inscriptionActiviteList).hasSize(databaseSizeBeforeCreate + 1);
        InscriptionActivite testInscriptionActivite = inscriptionActiviteList.get(inscriptionActiviteList.size() - 1);
        assertThat(testInscriptionActivite.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createInscriptionActiviteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscriptionActiviteRepository.findAll().size();

        // Create the InscriptionActivite with an existing ID
        inscriptionActivite.setId(1L);
        InscriptionActiviteDTO inscriptionActiviteDTO = inscriptionActiviteMapper.toDto(inscriptionActivite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionActiviteMockMvc.perform(post("/api/inscription-activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionActiviteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InscriptionActivite in the database
        List<InscriptionActivite> inscriptionActiviteList = inscriptionActiviteRepository.findAll();
        assertThat(inscriptionActiviteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInscriptionActivites() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionActivite.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }
    
    @Test
    @Transactional
    public void getInscriptionActivite() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get the inscriptionActivite
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites/{id}", inscriptionActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionActivite.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }


    @Test
    @Transactional
    public void getInscriptionActivitesByIdFiltering() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        Long id = inscriptionActivite.getId();

        defaultInscriptionActiviteShouldBeFound("id.equals=" + id);
        defaultInscriptionActiviteShouldNotBeFound("id.notEquals=" + id);

        defaultInscriptionActiviteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInscriptionActiviteShouldNotBeFound("id.greaterThan=" + id);

        defaultInscriptionActiviteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInscriptionActiviteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role equals to DEFAULT_ROLE
        defaultInscriptionActiviteShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the inscriptionActiviteList where role equals to UPDATED_ROLE
        defaultInscriptionActiviteShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role not equals to DEFAULT_ROLE
        defaultInscriptionActiviteShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the inscriptionActiviteList where role not equals to UPDATED_ROLE
        defaultInscriptionActiviteShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultInscriptionActiviteShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the inscriptionActiviteList where role equals to UPDATED_ROLE
        defaultInscriptionActiviteShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role is not null
        defaultInscriptionActiviteShouldBeFound("role.specified=true");

        // Get all the inscriptionActiviteList where role is null
        defaultInscriptionActiviteShouldNotBeFound("role.specified=false");
    }
                @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleContainsSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role contains DEFAULT_ROLE
        defaultInscriptionActiviteShouldBeFound("role.contains=" + DEFAULT_ROLE);

        // Get all the inscriptionActiviteList where role contains UPDATED_ROLE
        defaultInscriptionActiviteShouldNotBeFound("role.contains=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllInscriptionActivitesByRoleNotContainsSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        // Get all the inscriptionActiviteList where role does not contain DEFAULT_ROLE
        defaultInscriptionActiviteShouldNotBeFound("role.doesNotContain=" + DEFAULT_ROLE);

        // Get all the inscriptionActiviteList where role does not contain UPDATED_ROLE
        defaultInscriptionActiviteShouldBeFound("role.doesNotContain=" + UPDATED_ROLE);
    }


    @Test
    @Transactional
    public void getAllInscriptionActivitesByActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);
        Activite activite = ActiviteResourceIT.createEntity(em);
        em.persist(activite);
        em.flush();
        inscriptionActivite.setActivite(activite);
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);
        Long activiteId = activite.getId();

        // Get all the inscriptionActiviteList where activite equals to activiteId
        defaultInscriptionActiviteShouldBeFound("activiteId.equals=" + activiteId);

        // Get all the inscriptionActiviteList where activite equals to activiteId + 1
        defaultInscriptionActiviteShouldNotBeFound("activiteId.equals=" + (activiteId + 1));
    }


    @Test
    @Transactional
    public void getAllInscriptionActivitesByParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);
        Participant participant = ParticipantResourceIT.createEntity(em);
        em.persist(participant);
        em.flush();
        inscriptionActivite.setParticipant(participant);
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);
        Long participantId = participant.getId();

        // Get all the inscriptionActiviteList where participant equals to participantId
        defaultInscriptionActiviteShouldBeFound("participantId.equals=" + participantId);

        // Get all the inscriptionActiviteList where participant equals to participantId + 1
        defaultInscriptionActiviteShouldNotBeFound("participantId.equals=" + (participantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscriptionActiviteShouldBeFound(String filter) throws Exception {
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionActivite.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));

        // Check, that the count call also returns 1
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscriptionActiviteShouldNotBeFound(String filter) throws Exception {
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInscriptionActivite() throws Exception {
        // Get the inscriptionActivite
        restInscriptionActiviteMockMvc.perform(get("/api/inscription-activites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscriptionActivite() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        int databaseSizeBeforeUpdate = inscriptionActiviteRepository.findAll().size();

        // Update the inscriptionActivite
        InscriptionActivite updatedInscriptionActivite = inscriptionActiviteRepository.findById(inscriptionActivite.getId()).get();
        // Disconnect from session so that the updates on updatedInscriptionActivite are not directly saved in db
        em.detach(updatedInscriptionActivite);
        updatedInscriptionActivite
            .role(UPDATED_ROLE);
        InscriptionActiviteDTO inscriptionActiviteDTO = inscriptionActiviteMapper.toDto(updatedInscriptionActivite);

        restInscriptionActiviteMockMvc.perform(put("/api/inscription-activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionActiviteDTO)))
            .andExpect(status().isOk());

        // Validate the InscriptionActivite in the database
        List<InscriptionActivite> inscriptionActiviteList = inscriptionActiviteRepository.findAll();
        assertThat(inscriptionActiviteList).hasSize(databaseSizeBeforeUpdate);
        InscriptionActivite testInscriptionActivite = inscriptionActiviteList.get(inscriptionActiviteList.size() - 1);
        assertThat(testInscriptionActivite.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingInscriptionActivite() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionActiviteRepository.findAll().size();

        // Create the InscriptionActivite
        InscriptionActiviteDTO inscriptionActiviteDTO = inscriptionActiviteMapper.toDto(inscriptionActivite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionActiviteMockMvc.perform(put("/api/inscription-activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscriptionActiviteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InscriptionActivite in the database
        List<InscriptionActivite> inscriptionActiviteList = inscriptionActiviteRepository.findAll();
        assertThat(inscriptionActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInscriptionActivite() throws Exception {
        // Initialize the database
        inscriptionActiviteRepository.saveAndFlush(inscriptionActivite);

        int databaseSizeBeforeDelete = inscriptionActiviteRepository.findAll().size();

        // Delete the inscriptionActivite
        restInscriptionActiviteMockMvc.perform(delete("/api/inscription-activites/{id}", inscriptionActivite.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InscriptionActivite> inscriptionActiviteList = inscriptionActiviteRepository.findAll();
        assertThat(inscriptionActiviteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
