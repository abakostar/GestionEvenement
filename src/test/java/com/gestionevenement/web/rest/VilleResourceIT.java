package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Ville;
import com.gestionevenement.domain.Pays;
import com.gestionevenement.repository.VilleRepository;
import com.gestionevenement.service.VilleService;
import com.gestionevenement.service.dto.VilleDTO;
import com.gestionevenement.service.mapper.VilleMapper;
import com.gestionevenement.service.dto.VilleCriteria;
import com.gestionevenement.service.VilleQueryService;

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
 * Integration tests for the {@link VilleResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class VilleResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private VilleMapper villeMapper;

    @Autowired
    private VilleService villeService;

    @Autowired
    private VilleQueryService villeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVilleMockMvc;

    private Ville ville;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ville createEntity(EntityManager em) {
        Ville ville = new Ville()
            .nom(DEFAULT_NOM);
        return ville;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ville createUpdatedEntity(EntityManager em) {
        Ville ville = new Ville()
            .nom(UPDATED_NOM);
        return ville;
    }

    @BeforeEach
    public void initTest() {
        ville = createEntity(em);
    }

    @Test
    @Transactional
    public void createVille() throws Exception {
        int databaseSizeBeforeCreate = villeRepository.findAll().size();
        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);
        restVilleMockMvc.perform(post("/api/villes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isCreated());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeCreate + 1);
        Ville testVille = villeList.get(villeList.size() - 1);
        assertThat(testVille.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createVilleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = villeRepository.findAll().size();

        // Create the Ville with an existing ID
        ville.setId(1L);
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVilleMockMvc.perform(post("/api/villes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVilles() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
    
    @Test
    @Transactional
    public void getVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", ville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ville.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }


    @Test
    @Transactional
    public void getVillesByIdFiltering() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        Long id = ville.getId();

        defaultVilleShouldBeFound("id.equals=" + id);
        defaultVilleShouldNotBeFound("id.notEquals=" + id);

        defaultVilleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVilleShouldNotBeFound("id.greaterThan=" + id);

        defaultVilleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVilleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVillesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom equals to DEFAULT_NOM
        defaultVilleShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the villeList where nom equals to UPDATED_NOM
        defaultVilleShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllVillesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom not equals to DEFAULT_NOM
        defaultVilleShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the villeList where nom not equals to UPDATED_NOM
        defaultVilleShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllVillesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultVilleShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the villeList where nom equals to UPDATED_NOM
        defaultVilleShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllVillesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom is not null
        defaultVilleShouldBeFound("nom.specified=true");

        // Get all the villeList where nom is null
        defaultVilleShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllVillesByNomContainsSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom contains DEFAULT_NOM
        defaultVilleShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the villeList where nom contains UPDATED_NOM
        defaultVilleShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllVillesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        // Get all the villeList where nom does not contain DEFAULT_NOM
        defaultVilleShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the villeList where nom does not contain UPDATED_NOM
        defaultVilleShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllVillesByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);
        Pays pays = PaysResourceIT.createEntity(em);
        em.persist(pays);
        em.flush();
        ville.setPays(pays);
        villeRepository.saveAndFlush(ville);
        Long paysId = pays.getId();

        // Get all the villeList where pays equals to paysId
        defaultVilleShouldBeFound("paysId.equals=" + paysId);

        // Get all the villeList where pays equals to paysId + 1
        defaultVilleShouldNotBeFound("paysId.equals=" + (paysId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVilleShouldBeFound(String filter) throws Exception {
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ville.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restVilleMockMvc.perform(get("/api/villes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVilleShouldNotBeFound(String filter) throws Exception {
        restVilleMockMvc.perform(get("/api/villes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVilleMockMvc.perform(get("/api/villes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingVille() throws Exception {
        // Get the ville
        restVilleMockMvc.perform(get("/api/villes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        int databaseSizeBeforeUpdate = villeRepository.findAll().size();

        // Update the ville
        Ville updatedVille = villeRepository.findById(ville.getId()).get();
        // Disconnect from session so that the updates on updatedVille are not directly saved in db
        em.detach(updatedVille);
        updatedVille
            .nom(UPDATED_NOM);
        VilleDTO villeDTO = villeMapper.toDto(updatedVille);

        restVilleMockMvc.perform(put("/api/villes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isOk());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeUpdate);
        Ville testVille = villeList.get(villeList.size() - 1);
        assertThat(testVille.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void updateNonExistingVille() throws Exception {
        int databaseSizeBeforeUpdate = villeRepository.findAll().size();

        // Create the Ville
        VilleDTO villeDTO = villeMapper.toDto(ville);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVilleMockMvc.perform(put("/api/villes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(villeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ville in the database
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVille() throws Exception {
        // Initialize the database
        villeRepository.saveAndFlush(ville);

        int databaseSizeBeforeDelete = villeRepository.findAll().size();

        // Delete the ville
        restVilleMockMvc.perform(delete("/api/villes/{id}", ville.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ville> villeList = villeRepository.findAll();
        assertThat(villeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
