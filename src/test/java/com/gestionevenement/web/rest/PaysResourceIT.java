package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Pays;
import com.gestionevenement.repository.PaysRepository;
import com.gestionevenement.service.PaysService;
import com.gestionevenement.service.dto.PaysDTO;
import com.gestionevenement.service.mapper.PaysMapper;
import com.gestionevenement.service.dto.PaysCriteria;
import com.gestionevenement.service.PaysQueryService;

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
 * Integration tests for the {@link PaysResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaysResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private PaysRepository paysRepository;

    @Autowired
    private PaysMapper paysMapper;

    @Autowired
    private PaysService paysService;

    @Autowired
    private PaysQueryService paysQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaysMockMvc;

    private Pays pays;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createEntity(EntityManager em) {
        Pays pays = new Pays()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM);
        return pays;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pays createUpdatedEntity(EntityManager em) {
        Pays pays = new Pays()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM);
        return pays;
    }

    @BeforeEach
    public void initTest() {
        pays = createEntity(em);
    }

    @Test
    @Transactional
    public void createPays() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();
        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);
        restPaysMockMvc.perform(post("/api/pays").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isCreated());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate + 1);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPays.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createPaysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paysRepository.findAll().size();

        // Create the Pays with an existing ID
        pays.setId(1L);
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaysMockMvc.perform(post("/api/pays").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
    
    @Test
    @Transactional
    public void getPays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", pays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pays.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }


    @Test
    @Transactional
    public void getPaysByIdFiltering() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        Long id = pays.getId();

        defaultPaysShouldBeFound("id.equals=" + id);
        defaultPaysShouldNotBeFound("id.notEquals=" + id);

        defaultPaysShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaysShouldNotBeFound("id.greaterThan=" + id);

        defaultPaysShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaysShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaysByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code equals to DEFAULT_CODE
        defaultPaysShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the paysList where code equals to UPDATED_CODE
        defaultPaysShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code not equals to DEFAULT_CODE
        defaultPaysShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the paysList where code not equals to UPDATED_CODE
        defaultPaysShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPaysShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the paysList where code equals to UPDATED_CODE
        defaultPaysShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code is not null
        defaultPaysShouldBeFound("code.specified=true");

        // Get all the paysList where code is null
        defaultPaysShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaysByCodeContainsSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code contains DEFAULT_CODE
        defaultPaysShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the paysList where code contains UPDATED_CODE
        defaultPaysShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPaysByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where code does not contain DEFAULT_CODE
        defaultPaysShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the paysList where code does not contain UPDATED_CODE
        defaultPaysShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllPaysByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom equals to DEFAULT_NOM
        defaultPaysShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the paysList where nom equals to UPDATED_NOM
        defaultPaysShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPaysByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom not equals to DEFAULT_NOM
        defaultPaysShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the paysList where nom not equals to UPDATED_NOM
        defaultPaysShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPaysByNomIsInShouldWork() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultPaysShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the paysList where nom equals to UPDATED_NOM
        defaultPaysShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPaysByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom is not null
        defaultPaysShouldBeFound("nom.specified=true");

        // Get all the paysList where nom is null
        defaultPaysShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaysByNomContainsSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom contains DEFAULT_NOM
        defaultPaysShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the paysList where nom contains UPDATED_NOM
        defaultPaysShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllPaysByNomNotContainsSomething() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        // Get all the paysList where nom does not contain DEFAULT_NOM
        defaultPaysShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the paysList where nom does not contain UPDATED_NOM
        defaultPaysShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaysShouldBeFound(String filter) throws Exception {
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pays.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restPaysMockMvc.perform(get("/api/pays/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaysShouldNotBeFound(String filter) throws Exception {
        restPaysMockMvc.perform(get("/api/pays?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaysMockMvc.perform(get("/api/pays/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPays() throws Exception {
        // Get the pays
        restPaysMockMvc.perform(get("/api/pays/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Update the pays
        Pays updatedPays = paysRepository.findById(pays.getId()).get();
        // Disconnect from session so that the updates on updatedPays are not directly saved in db
        em.detach(updatedPays);
        updatedPays
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM);
        PaysDTO paysDTO = paysMapper.toDto(updatedPays);

        restPaysMockMvc.perform(put("/api/pays").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isOk());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
        Pays testPays = paysList.get(paysList.size() - 1);
        assertThat(testPays.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPays.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void updateNonExistingPays() throws Exception {
        int databaseSizeBeforeUpdate = paysRepository.findAll().size();

        // Create the Pays
        PaysDTO paysDTO = paysMapper.toDto(pays);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaysMockMvc.perform(put("/api/pays").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paysDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pays in the database
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePays() throws Exception {
        // Initialize the database
        paysRepository.saveAndFlush(pays);

        int databaseSizeBeforeDelete = paysRepository.findAll().size();

        // Delete the pays
        restPaysMockMvc.perform(delete("/api/pays/{id}", pays.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pays> paysList = paysRepository.findAll();
        assertThat(paysList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
