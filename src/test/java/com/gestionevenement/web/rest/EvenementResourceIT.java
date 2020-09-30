package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Evenement;
import com.gestionevenement.domain.Categorie;
import com.gestionevenement.repository.EvenementRepository;
import com.gestionevenement.service.EvenementService;
import com.gestionevenement.service.dto.EvenementDTO;
import com.gestionevenement.service.mapper.EvenementMapper;
import com.gestionevenement.service.dto.EvenementCriteria;
import com.gestionevenement.service.EvenementQueryService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.gestionevenement.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EvenementResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EvenementResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EvenementMapper evenementMapper;

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private EvenementQueryService evenementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvenementMockMvc;

    private Evenement evenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .code(DEFAULT_CODE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Categorie categorie;
        if (TestUtil.findAll(em, Categorie.class).isEmpty()) {
            categorie = CategorieResourceIT.createEntity(em);
            em.persist(categorie);
            em.flush();
        } else {
            categorie = TestUtil.findAll(em, Categorie.class).get(0);
        }
        evenement.setCategorie(categorie);
        return evenement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createUpdatedEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .code(UPDATED_CODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Categorie categorie;
        if (TestUtil.findAll(em, Categorie.class).isEmpty()) {
            categorie = CategorieResourceIT.createUpdatedEntity(em);
            em.persist(categorie);
            em.flush();
        } else {
            categorie = TestUtil.findAll(em, Categorie.class).get(0);
        }
        evenement.setCategorie(categorie);
        return evenement;
    }

    @BeforeEach
    public void initTest() {
        evenement = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvenement() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();
        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);
        restEvenementMockMvc.perform(post("/api/evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate + 1);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEvenement.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testEvenement.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testEvenement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createEvenementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // Create the Evenement with an existing ID
        evenement.setId(1L);
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvenementMockMvc.perform(post("/api/evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEvenements() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList
        restEvenementMockMvc.perform(get("/api/evenements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evenement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getEvenementsByIdFiltering() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        Long id = evenement.getId();

        defaultEvenementShouldBeFound("id.equals=" + id);
        defaultEvenementShouldNotBeFound("id.notEquals=" + id);

        defaultEvenementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEvenementShouldNotBeFound("id.greaterThan=" + id);

        defaultEvenementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEvenementShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEvenementsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code equals to DEFAULT_CODE
        defaultEvenementShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the evenementList where code equals to UPDATED_CODE
        defaultEvenementShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEvenementsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code not equals to DEFAULT_CODE
        defaultEvenementShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the evenementList where code not equals to UPDATED_CODE
        defaultEvenementShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEvenementsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEvenementShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the evenementList where code equals to UPDATED_CODE
        defaultEvenementShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEvenementsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code is not null
        defaultEvenementShouldBeFound("code.specified=true");

        // Get all the evenementList where code is null
        defaultEvenementShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvenementsByCodeContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code contains DEFAULT_CODE
        defaultEvenementShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the evenementList where code contains UPDATED_CODE
        defaultEvenementShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllEvenementsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where code does not contain DEFAULT_CODE
        defaultEvenementShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the evenementList where code does not contain UPDATED_CODE
        defaultEvenementShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut not equals to DEFAULT_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.notEquals=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut not equals to UPDATED_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.notEquals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the evenementList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut is not null
        defaultEvenementShouldBeFound("dateDebut.specified=true");

        // Get all the evenementList where dateDebut is null
        defaultEvenementShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultEvenementShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the evenementList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultEvenementShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin equals to DEFAULT_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin equals to UPDATED_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin not equals to DEFAULT_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.notEquals=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin not equals to UPDATED_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.notEquals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the evenementList where dateFin equals to UPDATED_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin is not null
        defaultEvenementShouldBeFound("dateFin.specified=true");

        // Get all the evenementList where dateFin is null
        defaultEvenementShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin is less than DEFAULT_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin is less than UPDATED_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateFin is greater than DEFAULT_DATE_FIN
        defaultEvenementShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the evenementList where dateFin is greater than SMALLER_DATE_FIN
        defaultEvenementShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }


    @Test
    @Transactional
    public void getAllEvenementsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description equals to DEFAULT_DESCRIPTION
        defaultEvenementShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description equals to UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description not equals to DEFAULT_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description not equals to UPDATED_DESCRIPTION
        defaultEvenementShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEvenementShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the evenementList where description equals to UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description is not null
        defaultEvenementShouldBeFound("description.specified=true");

        // Get all the evenementList where description is null
        defaultEvenementShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEvenementsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description contains DEFAULT_DESCRIPTION
        defaultEvenementShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description contains UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEvenementsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description does not contain DEFAULT_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description does not contain UPDATED_DESCRIPTION
        defaultEvenementShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEvenementsByCategorieIsEqualToSomething() throws Exception {
        // Get already existing entity
        Categorie categorie = evenement.getCategorie();
        evenementRepository.saveAndFlush(evenement);
        Long categorieId = categorie.getId();

        // Get all the evenementList where categorie equals to categorieId
        defaultEvenementShouldBeFound("categorieId.equals=" + categorieId);

        // Get all the evenementList where categorie equals to categorieId + 1
        defaultEvenementShouldNotBeFound("categorieId.equals=" + (categorieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvenementShouldBeFound(String filter) throws Exception {
        restEvenementMockMvc.perform(get("/api/evenements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restEvenementMockMvc.perform(get("/api/evenements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvenementShouldNotBeFound(String filter) throws Exception {
        restEvenementMockMvc.perform(get("/api/evenements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvenementMockMvc.perform(get("/api/evenements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEvenement() throws Exception {
        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement
        Evenement updatedEvenement = evenementRepository.findById(evenement.getId()).get();
        // Disconnect from session so that the updates on updatedEvenement are not directly saved in db
        em.detach(updatedEvenement);
        updatedEvenement
            .code(UPDATED_CODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .description(UPDATED_DESCRIPTION);
        EvenementDTO evenementDTO = evenementMapper.toDto(updatedEvenement);

        restEvenementMockMvc.perform(put("/api/evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEvenement.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testEvenement.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEvenement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementMockMvc.perform(put("/api/evenements").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeDelete = evenementRepository.findAll().size();

        // Delete the evenement
        restEvenementMockMvc.perform(delete("/api/evenements/{id}", evenement.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
