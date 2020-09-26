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

    private static final Boolean DEFAULT_ETATCLOS = false;
    private static final Boolean UPDATED_ETATCLOS = true;

    private static final LocalDate DEFAULT_DATE_ACTIVITE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACTIVITE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ACTIVITE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_HEURE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HEURE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HEURE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HEURE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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
            .etatclos(DEFAULT_ETATCLOS)
            .dateActivite(DEFAULT_DATE_ACTIVITE)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN);
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
            .etatclos(UPDATED_ETATCLOS)
            .dateActivite(UPDATED_DATE_ACTIVITE)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN);
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
        assertThat(testActivite.isEtatclos()).isEqualTo(DEFAULT_ETATCLOS);
        assertThat(testActivite.getDateActivite()).isEqualTo(DEFAULT_DATE_ACTIVITE);
        assertThat(testActivite.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testActivite.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
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
            .andExpect(jsonPath("$.[*].etatclos").value(hasItem(DEFAULT_ETATCLOS.booleanValue())))
            .andExpect(jsonPath("$.[*].dateActivite").value(hasItem(DEFAULT_DATE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(sameInstant(DEFAULT_HEURE_DEBUT))))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(sameInstant(DEFAULT_HEURE_FIN))));
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
            .andExpect(jsonPath("$.etatclos").value(DEFAULT_ETATCLOS.booleanValue()))
            .andExpect(jsonPath("$.dateActivite").value(DEFAULT_DATE_ACTIVITE.toString()))
            .andExpect(jsonPath("$.heureDebut").value(sameInstant(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.heureFin").value(sameInstant(DEFAULT_HEURE_FIN)));
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
    public void getAllActivitesByDateActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite equals to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.equals=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.equals=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite not equals to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.notEquals=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite not equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.notEquals=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite in DEFAULT_DATE_ACTIVITE or UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.in=" + DEFAULT_DATE_ACTIVITE + "," + UPDATED_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite equals to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.in=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite is not null
        defaultActiviteShouldBeFound("dateActivite.specified=true");

        // Get all the activiteList where dateActivite is null
        defaultActiviteShouldNotBeFound("dateActivite.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite is greater than or equal to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.greaterThanOrEqual=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite is greater than or equal to UPDATED_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.greaterThanOrEqual=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite is less than or equal to DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.lessThanOrEqual=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite is less than or equal to SMALLER_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.lessThanOrEqual=" + SMALLER_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite is less than DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.lessThan=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite is less than UPDATED_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.lessThan=" + UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    public void getAllActivitesByDateActiviteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateActivite is greater than DEFAULT_DATE_ACTIVITE
        defaultActiviteShouldNotBeFound("dateActivite.greaterThan=" + DEFAULT_DATE_ACTIVITE);

        // Get all the activiteList where dateActivite is greater than SMALLER_DATE_ACTIVITE
        defaultActiviteShouldBeFound("dateActivite.greaterThan=" + SMALLER_DATE_ACTIVITE);
    }


    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut equals to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.equals=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.equals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut not equals to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.notEquals=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut not equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.notEquals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut in DEFAULT_HEURE_DEBUT or UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.in=" + DEFAULT_HEURE_DEBUT + "," + UPDATED_HEURE_DEBUT);

        // Get all the activiteList where heureDebut equals to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.in=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut is not null
        defaultActiviteShouldBeFound("heureDebut.specified=true");

        // Get all the activiteList where heureDebut is null
        defaultActiviteShouldNotBeFound("heureDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut is greater than or equal to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.greaterThanOrEqual=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut is greater than or equal to UPDATED_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.greaterThanOrEqual=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut is less than or equal to DEFAULT_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.lessThanOrEqual=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut is less than or equal to SMALLER_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.lessThanOrEqual=" + SMALLER_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut is less than DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.lessThan=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut is less than UPDATED_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.lessThan=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureDebut is greater than DEFAULT_HEURE_DEBUT
        defaultActiviteShouldNotBeFound("heureDebut.greaterThan=" + DEFAULT_HEURE_DEBUT);

        // Get all the activiteList where heureDebut is greater than SMALLER_HEURE_DEBUT
        defaultActiviteShouldBeFound("heureDebut.greaterThan=" + SMALLER_HEURE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin equals to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.equals=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin equals to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.equals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin not equals to DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.notEquals=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin not equals to UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.notEquals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin in DEFAULT_HEURE_FIN or UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.in=" + DEFAULT_HEURE_FIN + "," + UPDATED_HEURE_FIN);

        // Get all the activiteList where heureFin equals to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.in=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin is not null
        defaultActiviteShouldBeFound("heureFin.specified=true");

        // Get all the activiteList where heureFin is null
        defaultActiviteShouldNotBeFound("heureFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin is greater than or equal to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.greaterThanOrEqual=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin is greater than or equal to UPDATED_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.greaterThanOrEqual=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin is less than or equal to DEFAULT_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.lessThanOrEqual=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin is less than or equal to SMALLER_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.lessThanOrEqual=" + SMALLER_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin is less than DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.lessThan=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin is less than UPDATED_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.lessThan=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllActivitesByHeureFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureFin is greater than DEFAULT_HEURE_FIN
        defaultActiviteShouldNotBeFound("heureFin.greaterThan=" + DEFAULT_HEURE_FIN);

        // Get all the activiteList where heureFin is greater than SMALLER_HEURE_FIN
        defaultActiviteShouldBeFound("heureFin.greaterThan=" + SMALLER_HEURE_FIN);
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
            .andExpect(jsonPath("$.[*].etatclos").value(hasItem(DEFAULT_ETATCLOS.booleanValue())))
            .andExpect(jsonPath("$.[*].dateActivite").value(hasItem(DEFAULT_DATE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(sameInstant(DEFAULT_HEURE_DEBUT))))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(sameInstant(DEFAULT_HEURE_FIN))));

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
            .etatclos(UPDATED_ETATCLOS)
            .dateActivite(UPDATED_DATE_ACTIVITE)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN);
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
        assertThat(testActivite.isEtatclos()).isEqualTo(UPDATED_ETATCLOS);
        assertThat(testActivite.getDateActivite()).isEqualTo(UPDATED_DATE_ACTIVITE);
        assertThat(testActivite.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testActivite.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
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
