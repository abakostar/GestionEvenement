package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Emplacement;
import com.gestionevenement.domain.Ville;
import com.gestionevenement.repository.EmplacementRepository;
import com.gestionevenement.service.EmplacementService;
import com.gestionevenement.service.dto.EmplacementDTO;
import com.gestionevenement.service.mapper.EmplacementMapper;
import com.gestionevenement.service.dto.EmplacementCriteria;
import com.gestionevenement.service.EmplacementQueryService;

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
 * Integration tests for the {@link EmplacementResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EmplacementResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITE = 1;
    private static final Integer UPDATED_CAPACITE = 2;
    private static final Integer SMALLER_CAPACITE = 1 - 1;

    @Autowired
    private EmplacementRepository emplacementRepository;

    @Autowired
    private EmplacementMapper emplacementMapper;

    @Autowired
    private EmplacementService emplacementService;

    @Autowired
    private EmplacementQueryService emplacementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmplacementMockMvc;

    private Emplacement emplacement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emplacement createEntity(EntityManager em) {
        Emplacement emplacement = new Emplacement()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .capacite(DEFAULT_CAPACITE);
        return emplacement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emplacement createUpdatedEntity(EntityManager em) {
        Emplacement emplacement = new Emplacement()
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .capacite(UPDATED_CAPACITE);
        return emplacement;
    }

    @BeforeEach
    public void initTest() {
        emplacement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmplacement() throws Exception {
        int databaseSizeBeforeCreate = emplacementRepository.findAll().size();
        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);
        restEmplacementMockMvc.perform(post("/api/emplacements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emplacementDTO)))
            .andExpect(status().isCreated());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeCreate + 1);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmplacement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmplacement.getCapacite()).isEqualTo(DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    public void createEmplacementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emplacementRepository.findAll().size();

        // Create the Emplacement with an existing ID
        emplacement.setId(1L);
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmplacementMockMvc.perform(post("/api/emplacements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emplacementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmplacements() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList
        restEmplacementMockMvc.perform(get("/api/emplacements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emplacement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)));
    }
    
    @Test
    @Transactional
    public void getEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get the emplacement
        restEmplacementMockMvc.perform(get("/api/emplacements/{id}", emplacement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emplacement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE));
    }


    @Test
    @Transactional
    public void getEmplacementsByIdFiltering() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        Long id = emplacement.getId();

        defaultEmplacementShouldBeFound("id.equals=" + id);
        defaultEmplacementShouldNotBeFound("id.notEquals=" + id);

        defaultEmplacementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmplacementShouldNotBeFound("id.greaterThan=" + id);

        defaultEmplacementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmplacementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmplacementsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code equals to DEFAULT_CODE
        defaultEmplacementShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the emplacementList where code equals to UPDATED_CODE
        defaultEmplacementShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code not equals to DEFAULT_CODE
        defaultEmplacementShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the emplacementList where code not equals to UPDATED_CODE
        defaultEmplacementShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEmplacementShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the emplacementList where code equals to UPDATED_CODE
        defaultEmplacementShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code is not null
        defaultEmplacementShouldBeFound("code.specified=true");

        // Get all the emplacementList where code is null
        defaultEmplacementShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmplacementsByCodeContainsSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code contains DEFAULT_CODE
        defaultEmplacementShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the emplacementList where code contains UPDATED_CODE
        defaultEmplacementShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where code does not contain DEFAULT_CODE
        defaultEmplacementShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the emplacementList where code does not contain UPDATED_CODE
        defaultEmplacementShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllEmplacementsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description equals to DEFAULT_DESCRIPTION
        defaultEmplacementShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the emplacementList where description equals to UPDATED_DESCRIPTION
        defaultEmplacementShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description not equals to DEFAULT_DESCRIPTION
        defaultEmplacementShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the emplacementList where description not equals to UPDATED_DESCRIPTION
        defaultEmplacementShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEmplacementShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the emplacementList where description equals to UPDATED_DESCRIPTION
        defaultEmplacementShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description is not null
        defaultEmplacementShouldBeFound("description.specified=true");

        // Get all the emplacementList where description is null
        defaultEmplacementShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmplacementsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description contains DEFAULT_DESCRIPTION
        defaultEmplacementShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the emplacementList where description contains UPDATED_DESCRIPTION
        defaultEmplacementShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where description does not contain DEFAULT_DESCRIPTION
        defaultEmplacementShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the emplacementList where description does not contain UPDATED_DESCRIPTION
        defaultEmplacementShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite equals to DEFAULT_CAPACITE
        defaultEmplacementShouldBeFound("capacite.equals=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite equals to UPDATED_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.equals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite not equals to DEFAULT_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.notEquals=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite not equals to UPDATED_CAPACITE
        defaultEmplacementShouldBeFound("capacite.notEquals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsInShouldWork() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite in DEFAULT_CAPACITE or UPDATED_CAPACITE
        defaultEmplacementShouldBeFound("capacite.in=" + DEFAULT_CAPACITE + "," + UPDATED_CAPACITE);

        // Get all the emplacementList where capacite equals to UPDATED_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.in=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsNullOrNotNull() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite is not null
        defaultEmplacementShouldBeFound("capacite.specified=true");

        // Get all the emplacementList where capacite is null
        defaultEmplacementShouldNotBeFound("capacite.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite is greater than or equal to DEFAULT_CAPACITE
        defaultEmplacementShouldBeFound("capacite.greaterThanOrEqual=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite is greater than or equal to UPDATED_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.greaterThanOrEqual=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite is less than or equal to DEFAULT_CAPACITE
        defaultEmplacementShouldBeFound("capacite.lessThanOrEqual=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite is less than or equal to SMALLER_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.lessThanOrEqual=" + SMALLER_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsLessThanSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite is less than DEFAULT_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.lessThan=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite is less than UPDATED_CAPACITE
        defaultEmplacementShouldBeFound("capacite.lessThan=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void getAllEmplacementsByCapaciteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList where capacite is greater than DEFAULT_CAPACITE
        defaultEmplacementShouldNotBeFound("capacite.greaterThan=" + DEFAULT_CAPACITE);

        // Get all the emplacementList where capacite is greater than SMALLER_CAPACITE
        defaultEmplacementShouldBeFound("capacite.greaterThan=" + SMALLER_CAPACITE);
    }


    @Test
    @Transactional
    public void getAllEmplacementsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);
        Ville ville = VilleResourceIT.createEntity(em);
        em.persist(ville);
        em.flush();
        emplacement.setVille(ville);
        emplacementRepository.saveAndFlush(emplacement);
        Long villeId = ville.getId();

        // Get all the emplacementList where ville equals to villeId
        defaultEmplacementShouldBeFound("villeId.equals=" + villeId);

        // Get all the emplacementList where ville equals to villeId + 1
        defaultEmplacementShouldNotBeFound("villeId.equals=" + (villeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmplacementShouldBeFound(String filter) throws Exception {
        restEmplacementMockMvc.perform(get("/api/emplacements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emplacement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)));

        // Check, that the count call also returns 1
        restEmplacementMockMvc.perform(get("/api/emplacements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmplacementShouldNotBeFound(String filter) throws Exception {
        restEmplacementMockMvc.perform(get("/api/emplacements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmplacementMockMvc.perform(get("/api/emplacements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEmplacement() throws Exception {
        // Get the emplacement
        restEmplacementMockMvc.perform(get("/api/emplacements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();

        // Update the emplacement
        Emplacement updatedEmplacement = emplacementRepository.findById(emplacement.getId()).get();
        // Disconnect from session so that the updates on updatedEmplacement are not directly saved in db
        em.detach(updatedEmplacement);
        updatedEmplacement
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .capacite(UPDATED_CAPACITE);
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(updatedEmplacement);

        restEmplacementMockMvc.perform(put("/api/emplacements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emplacementDTO)))
            .andExpect(status().isOk());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmplacement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmplacement.getCapacite()).isEqualTo(UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmplacementMockMvc.perform(put("/api/emplacements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(emplacementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeDelete = emplacementRepository.findAll().size();

        // Delete the emplacement
        restEmplacementMockMvc.perform(delete("/api/emplacements/{id}", emplacement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
