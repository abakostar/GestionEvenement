package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Activite;
import com.gestionevenement.domain.Evenement;
import com.gestionevenement.domain.Emplacement;
import com.gestionevenement.repository.ActiviteRepository;
import com.gestionevenement.service.ActiviteService;
import com.gestionevenement.service.dto.ActiviteDTO;
import com.gestionevenement.service.mapper.ActiviteMapper;
import com.gestionevenement.service.dto.ActiviteCriteria;
import com.gestionevenement.service.ActiviteQueryService;

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
import java.time.LocalDate;
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
 * Integration tests for the {@link ActiviteResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ActiviteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ACTIVITE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACTIVITE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ACTIVITE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_HEURE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HEURE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HEURE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HEURE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ETATCLOS = false;
    private static final Boolean UPDATED_ETATCLOS = true;

    @Autowired
    private ActiviteRepository activiteRepository;

    @Autowired
    private ActiviteMapper activiteMapper;

    @Autowired
    private ActiviteService activiteService;

    @Autowired
    private ActiviteQueryService activiteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteMockMvc;

    private Activite activite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createEntity(EntityManager em) {
        Activite activite = new Activite()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .date_activite(DEFAULT_DATE_ACTIVITE)
            .heure_debut(DEFAULT_HEURE_DEBUT)
            .heure_fin(DEFAULT_HEURE_FIN)
            .etatclos(DEFAULT_ETATCLOS);
        return activite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createUpdatedEntity(EntityManager em) {
        Activite activite = new Activite()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .date_activite(UPDATED_DATE_ACTIVITE)
            .heure_debut(UPDATED_HEURE_DEBUT)
            .heure_fin(UPDATED_HEURE_FIN)
            .etatclos(UPDATED_ETATCLOS);
        return activite;
    }

    @BeforeEach
    public void initTest() {
        activite = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivite() throws Exception {
        int databaseSizeBeforeCreate = activiteRepository.findAll().size();
        // Create the Activite
        ActiviteDTO activiteDTO = activiteMapper.toDto(activite);
        restActiviteMockMvc.perform(post("/api/activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activiteDTO)))
            .andExpect(status().isCreated());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate + 1);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testActivite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testActivite.getDate_activite()).isEqualTo(DEFAULT_DATE_ACTIVITE);
        assertThat(testActivite.getHeure_debut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testActivite.getHeure_fin()).isEqualTo(DEFAULT_HEURE_FIN);
        assertThat(testActivite.isEtatclos()).isEqualTo(DEFAULT_ETATCLOS);
    }

    @Test
    @Transactional
    public void createActiviteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activiteRepository.findAll().size();

        // Create the Activite with an existing ID
        activite.setId(1L);
        ActiviteDTO activiteDTO = activiteMapper.toDto(activite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteMockMvc.perform(post("/api/activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActivites() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList
        restActiviteMockMvc.perform(get("/api/activites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date_activite").value(hasItem(DEFAULT_DATE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].heure_debut").value(hasItem(sameInstant(DEFAULT_HEURE_DEBUT))))
            .andExpect(jsonPath("$.[*].heure_fin").value(hasItem(sameInstant(DEFAULT_HEURE_FIN))))
            .andExpect(jsonPath("$.[*].etatclos").value(hasItem(DEFAULT_ETATCLOS.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get the activite
        restActiviteMockMvc.perform(get("/api/activites/{id}", activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activite.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date_activite").value(DEFAULT_DATE_ACTIVITE.toString()))
            .andExpect(jsonPath("$.heure_debut").value(sameInstant(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.heure_fin").value(sameInstant(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.etatclos").value(DEFAULT_ETATCLOS.booleanValue()));
    }


    @Test
    @Transactional
    public void getActivitesByIdFiltering() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        Long id = activite.getId();

        defaultActiviteShouldBeFound("id.equals=" + id);
        defaultActiviteShouldNotBeFound("id.notEquals=" + id);

        defaultActiviteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActiviteShouldNotBeFound("id.greaterThan=" + id);

        defaultActiviteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActiviteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllActivitesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom equals to DEFAULT_NOM
        defaultActiviteShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the activiteList where nom equals to UPDATED_NOM
        defaultActiviteShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivitesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom not equals to DEFAULT_NOM
        defaultActiviteShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the activiteList where nom not equals to UPDATED_NOM
        defaultActiviteShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivitesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultActiviteShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the activiteList where nom equals to UPDATED_NOM
        defaultActiviteShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivitesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom is not null
        defaultActiviteShouldBeFound("nom.specified=true");

        // Get all the activiteList where nom is null
        defaultActiviteShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllActivitesByNomContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom contains DEFAULT_NOM
        defaultActiviteShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the activiteList where nom contains UPDATED_NOM
        defaultActiviteShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllActivitesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where nom does not contain DEFAULT_NOM
        defaultActiviteShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the activiteList where nom does not contain UPDATED_NOM
        defaultActiviteShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllActivitesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description equals to DEFAULT_DESCRIPTION
        defaultActiviteShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the activiteList where description equals to UPDATED_DESCRIPTION
        defaultActiviteShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActivitesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description not equals to DEFAULT_DESCRIPTION
        defaultActiviteShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the activiteList where description not equals to UPDATED_DESCRIPTION
        defaultActiviteShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActivitesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultActiviteShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the activiteList where description equals to UPDATED_DESCRIPTION
        defaultActiviteShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActivitesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description is not null
        defaultActiviteShouldBeFound("description.specified=true");

        // Get all the activiteList where description is null
        defaultActiviteShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllActivitesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description contains DEFAULT_DESCRIPTION
        defaultActiviteShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the activiteList where description contains UPDATED_DESCRIPTION
        defaultActiviteShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllActivitesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where description does not contain DEFAULT_DESCRIPTION
        defaultActiviteShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the activiteList where description does not contain UPDATED_DESCRIPTION
        defaultActiviteShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite equals to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.equals=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.equals=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite not equals to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.notEquals=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite not equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.notEquals=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite in DEFAULT_DATE_ACTIVITE or UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.in=" + DEFAULT_DATE_ACTIVITE + "," + UPDATED_DATE_ACTIVITE);

        // Get all the activiteList where date_activite equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.in=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite is not null
        defaultActiviteShouldBeFound("date_activite.specified=true");

        // Get all the activiteList where date_activite is null
        defaultActiviteShouldNotBeFound("date_activite.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite is greater than or equal to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.greaterThanOrEqual=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite is greater than or equal to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.greaterThanOrEqual=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite is less than or equal to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.lessThanOrEqual=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite is less than or equal to SMALLER_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.lessThanOrEqual=" + SMALLER_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite is less than DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.lessThan=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite is less than UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.lessThan=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDate_activiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where date_activite is greater than DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("date_activite.greaterThan=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where date_activite is greater than SMALLER_DATE_ACTIVITE
        defaultActiviteShouldBeFound("date_activite.greaterThan=" + SMALLER_DATE_ACTIVITE);
    }


    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut equals to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.equals=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.equals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut not equals to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.notEquals=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut not equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.notEquals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut in DEFAULT_HEURE_DEBUT or UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.in=" + DEFAULT_HEURE_DEBUT + "," + UPDATED_HEURE_DEBUT);

        // Get all the activiteList where heure_debut equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.in=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut is not null
        defaultActiviteShouldBeFound("heure_debut.specified=true");

        // Get all the activiteList where heure_debut is null
        defaultActiviteShouldNotBeFound("heure_debut.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut is greater than or equal to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.greaterThanOrEqual=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut is greater than or equal to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.greaterThanOrEqual=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut is less than or equal to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.lessThanOrEqual=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut is less than or equal to SMALLER_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.lessThanOrEqual=" + SMALLER_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut is less than DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.lessThan=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut is less than UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.lessThan=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_debutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_debut is greater than DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heure_debut.greaterThan=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heure_debut is greater than SMALLER_HEURE_DEBUT
        defaultActiviteShouldBeFound("heure_debut.greaterThan=" + SMALLER_HEURE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin equals to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.equals=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin equals to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.equals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin not equals to DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.notEquals=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin not equals to UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.notEquals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin in DEFAULT_HEURE_FIN or UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.in=" + DEFAULT_HEURE_FIN + "," + UPDATED_HEURE_FIN);

        // Get all the activiteList where heure_fin equals to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.in=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin is not null
        defaultActiviteShouldBeFound("heure_fin.specified=true");

        // Get all the activiteList where heure_fin is null
        defaultActiviteShouldNotBeFound("heure_fin.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin is greater than or equal to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.greaterThanOrEqual=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin is greater than or equal to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.greaterThanOrEqual=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin is less than or equal to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.lessThanOrEqual=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin is less than or equal to SMALLER_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.lessThanOrEqual=" + SMALLER_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin is less than DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.lessThan=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin is less than UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.lessThan=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeure_finIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heure_fin is greater than DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heure_fin.greaterThan=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heure_fin is greater than SMALLER_HEURE_FIN
        defaultActiviteShouldBeFound("heure_fin.greaterThan=" + SMALLER_HEURE_FIN);
    }


    @Test
    @Transactional
    public void getAllActivitesByEtatclosIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where etatclos equals to DEFAULT_ETATCLOS
        defaultActiviteShouldBeFound("etatclos.equals=" + DEFAULT_ETATCLOS);

        // Get all the activiteList where etatclos equals to UPDATED_ETATCLOS
        defaultActiviteShouldNotBeFound("etatclos.equals=" + UPDATED_ETATCLOS);
    }

    @Test
    @Transactional
    public void getAllActivitesByEtatclosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where etatclos not equals to DEFAULT_ETATCLOS
        defaultActiviteShouldNotBeFound("etatclos.notEquals=" + DEFAULT_ETATCLOS);

        // Get all the activiteList where etatclos not equals to UPDATED_ETATCLOS
        defaultActiviteShouldBeFound("etatclos.notEquals=" + UPDATED_ETATCLOS);
    }

    @Test
    @Transactional
    public void getAllActivitesByEtatclosIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where etatclos in DEFAULT_ETATCLOS or UPDATED_ETATCLOS
        defaultActiviteShouldBeFound("etatclos.in=" + DEFAULT_ETATCLOS + "," + UPDATED_ETATCLOS);

        // Get all the activiteList where etatclos equals to UPDATED_ETATCLOS
        defaultActiviteShouldNotBeFound("etatclos.in=" + UPDATED_ETATCLOS);
    }

    @Test
    @Transactional
    public void getAllActivitesByEtatclosIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where etatclos is not null
        defaultActiviteShouldBeFound("etatclos.specified=true");

        // Get all the activiteList where etatclos is null
        defaultActiviteShouldNotBeFound("etatclos.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);
        Evenement evenement = EvenementResourceIT.createEntity(em);
        em.persist(evenement);
        em.flush();
        activite.setEvenement(evenement);
        activiteRepository.saveAndFlush(activite);
        Long evenementId = evenement.getId();

        // Get all the activiteList where evenement equals to evenementId
        defaultActiviteShouldBeFound("evenementId.equals=" + evenementId);

        // Get all the activiteList where evenement equals to evenementId + 1
        defaultActiviteShouldNotBeFound("evenementId.equals=" + (evenementId + 1));
    }


    @Test
    @Transactional
    public void getAllActivitesByEmplacementIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);
        Emplacement emplacement = EmplacementResourceIT.createEntity(em);
        em.persist(emplacement);
        em.flush();
        activite.setEmplacement(emplacement);
        activiteRepository.saveAndFlush(activite);
        Long emplacementId = emplacement.getId();

        // Get all the activiteList where emplacement equals to emplacementId
        defaultActiviteShouldBeFound("emplacementId.equals=" + emplacementId);

        // Get all the activiteList where emplacement equals to emplacementId + 1
        defaultActiviteShouldNotBeFound("emplacementId.equals=" + (emplacementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActiviteShouldBeFound(String filter) throws Exception {
        restActiviteMockMvc.perform(get("/api/activites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date_activite").value(hasItem(DEFAULT_DATE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].heure_debut").value(hasItem(sameInstant(DEFAULT_HEURE_DEBUT))))
            .andExpect(jsonPath("$.[*].heure_fin").value(hasItem(sameInstant(DEFAULT_HEURE_FIN))))
            .andExpect(jsonPath("$.[*].etatclos").value(hasItem(DEFAULT_ETATCLOS.booleanValue())));

        // Check, that the count call also returns 1
        restActiviteMockMvc.perform(get("/api/activites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActiviteShouldNotBeFound(String filter) throws Exception {
        restActiviteMockMvc.perform(get("/api/activites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActiviteMockMvc.perform(get("/api/activites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingActivite() throws Exception {
        // Get the activite
        restActiviteMockMvc.perform(get("/api/activites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite
        Activite updatedActivite = activiteRepository.findById(activite.getId()).get();
        // Disconnect from session so that the updates on updatedActivite are not directly saved in db
        em.detach(updatedActivite);
        updatedActivite
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .date_activite(UPDATED_DATE_ACTIVITE)
            .heure_debut(UPDATED_HEURE_DEBUT)
            .heure_fin(UPDATED_HEURE_FIN)
            .etatclos(UPDATED_ETATCLOS);
        ActiviteDTO activiteDTO = activiteMapper.toDto(updatedActivite);

        restActiviteMockMvc.perform(put("/api/activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activiteDTO)))
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testActivite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testActivite.getDate_activite()).isEqualTo(UPDATED_DATE_ACTIVITE);
        assertThat(testActivite.getHeure_debut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testActivite.getHeure_fin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testActivite.isEtatclos()).isEqualTo(UPDATED_ETATCLOS);
    }

    @Test
    @Transactional
    public void updateNonExistingActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Create the Activite
        ActiviteDTO activiteDTO = activiteMapper.toDto(activite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc.perform(put("/api/activites").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeDelete = activiteRepository.findAll().size();

        // Delete the activite
        restActiviteMockMvc.perform(delete("/api/activites/{id}", activite.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
