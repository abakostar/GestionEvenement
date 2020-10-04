package com.gestionevenement.web.rest;

import com.gestionevenement.GestionevenementappApp;
import com.gestionevenement.domain.Participant;
import com.gestionevenement.domain.Ville;
import com.gestionevenement.repository.ParticipantRepository;
import com.gestionevenement.service.ParticipantService;
import com.gestionevenement.service.dto.ParticipantDTO;
import com.gestionevenement.service.mapper.ParticipantMapper;
import com.gestionevenement.service.ParticipantQueryService;

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
 * Integration tests for the {@link ParticipantResource} REST controller.
 */
@SpringBootTest(classes = GestionevenementappApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParticipantResourceIT {

    private static final String DEFAULT_SEXE = "AAAAAAAAAA";
    private static final String UPDATED_SEXE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_LANG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LANG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantQueryService participantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipantMockMvc;

    private Participant participant;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participant createEntity(EntityManager em) {
        Participant participant = new Participant()
            .sexe(DEFAULT_SEXE)
            .telephone(DEFAULT_TELEPHONE)
            .login(DEFAULT_LOGIN);
        return participant;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participant createUpdatedEntity(EntityManager em) {
        Participant participant = new Participant()
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .login(UPDATED_LOGIN);
        return participant;
    }

    @BeforeEach
    public void initTest() {
        participant = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();
        // Create the Participant
        ParticipantDTO participantDTO = participantMapper.toDto(participant);
        participantDTO.setLogin(DEFAULT_LOGIN);
        participantDTO.setPassword("password");
        participantDTO.setFirstName("FirstName");
        participantDTO.setLastName("LastName");
        participantDTO.setEmail("email@gmail.com");
        restParticipantMockMvc.perform(post("/api/participants").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testParticipant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testParticipant.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    public void createParticipantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant with an existing ID
        participant.setId(1L);
        ParticipantDTO participantDTO = participantMapper.toDto(participant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantMockMvc.perform(post("/api/participants").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @Transactional
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participant.getId().intValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN));
    }


    @Test
    @Transactional
    public void getParticipantsByIdFiltering() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        Long id = participant.getId();

        defaultParticipantShouldBeFound("id.equals=" + id);
        defaultParticipantShouldNotBeFound("id.notEquals=" + id);

        defaultParticipantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParticipantShouldNotBeFound("id.greaterThan=" + id);

        defaultParticipantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParticipantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllParticipantsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe equals to DEFAULT_SEXE
        defaultParticipantShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the participantList where sexe equals to UPDATED_SEXE
        defaultParticipantShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllParticipantsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe not equals to DEFAULT_SEXE
        defaultParticipantShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the participantList where sexe not equals to UPDATED_SEXE
        defaultParticipantShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllParticipantsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultParticipantShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the participantList where sexe equals to UPDATED_SEXE
        defaultParticipantShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllParticipantsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe is not null
        defaultParticipantShouldBeFound("sexe.specified=true");

        // Get all the participantList where sexe is null
        defaultParticipantShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsBySexeContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe contains DEFAULT_SEXE
        defaultParticipantShouldBeFound("sexe.contains=" + DEFAULT_SEXE);

        // Get all the participantList where sexe contains UPDATED_SEXE
        defaultParticipantShouldNotBeFound("sexe.contains=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllParticipantsBySexeNotContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where sexe does not contain DEFAULT_SEXE
        defaultParticipantShouldNotBeFound("sexe.doesNotContain=" + DEFAULT_SEXE);

        // Get all the participantList where sexe does not contain UPDATED_SEXE
        defaultParticipantShouldBeFound("sexe.doesNotContain=" + UPDATED_SEXE);
    }


    @Test
    @Transactional
    public void getAllParticipantsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone equals to DEFAULT_TELEPHONE
        defaultParticipantShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the participantList where telephone equals to UPDATED_TELEPHONE
        defaultParticipantShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone not equals to DEFAULT_TELEPHONE
        defaultParticipantShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the participantList where telephone not equals to UPDATED_TELEPHONE
        defaultParticipantShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultParticipantShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the participantList where telephone equals to UPDATED_TELEPHONE
        defaultParticipantShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone is not null
        defaultParticipantShouldBeFound("telephone.specified=true");

        // Get all the participantList where telephone is null
        defaultParticipantShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone contains DEFAULT_TELEPHONE
        defaultParticipantShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the participantList where telephone contains UPDATED_TELEPHONE
        defaultParticipantShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where telephone does not contain DEFAULT_TELEPHONE
        defaultParticipantShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the participantList where telephone does not contain UPDATED_TELEPHONE
        defaultParticipantShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login equals to DEFAULT_LOGIN
        defaultParticipantShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the participantList where login equals to UPDATED_LOGIN
        defaultParticipantShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login not equals to DEFAULT_LOGIN
        defaultParticipantShouldNotBeFound("login.notEquals=" + DEFAULT_LOGIN);

        // Get all the participantList where login not equals to UPDATED_LOGIN
        defaultParticipantShouldBeFound("login.notEquals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultParticipantShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the participantList where login equals to UPDATED_LOGIN
        defaultParticipantShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login is not null
        defaultParticipantShouldBeFound("login.specified=true");

        // Get all the participantList where login is null
        defaultParticipantShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login contains DEFAULT_LOGIN
        defaultParticipantShouldBeFound("login.contains=" + DEFAULT_LOGIN);

        // Get all the participantList where login contains UPDATED_LOGIN
        defaultParticipantShouldNotBeFound("login.contains=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllParticipantsByLoginNotContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where login does not contain DEFAULT_LOGIN
        defaultParticipantShouldNotBeFound("login.doesNotContain=" + DEFAULT_LOGIN);

        // Get all the participantList where login does not contain UPDATED_LOGIN
        defaultParticipantShouldBeFound("login.doesNotContain=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllParticipantsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where password contains DEFAULT_PASSWORD
        defaultParticipantShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the participantList where password contains UPDATED_PASSWORD
    }




    @Test
    @Transactional
    public void getAllParticipantsByVilleIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        Ville ville = VilleResourceIT.createEntity(em);
        em.persist(ville);
        em.flush();
        participant.setVille(ville);
        participantRepository.saveAndFlush(participant);
        Long villeId = ville.getId();

        // Get all the participantList where ville equals to villeId
        defaultParticipantShouldBeFound("villeId.equals=" + villeId);

        // Get all the participantList where ville equals to villeId + 1
        defaultParticipantShouldNotBeFound("villeId.equals=" + (villeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParticipantShouldBeFound(String filter) throws Exception {
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));

        // Check, that the count call also returns 1
        restParticipantMockMvc.perform(get("/api/participants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParticipantShouldNotBeFound(String filter) throws Exception {
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParticipantMockMvc.perform(get("/api/participants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        Participant updatedParticipant = participantRepository.findById(participant.getId()).get();
        // Disconnect from session so that the updates on updatedParticipant are not directly saved in db
        em.detach(updatedParticipant);
        updatedParticipant
            .sexe(UPDATED_SEXE)
            .telephone(UPDATED_TELEPHONE)
            .login(UPDATED_LOGIN);
        ParticipantDTO participantDTO = participantMapper.toDto(updatedParticipant);

        restParticipantMockMvc.perform(put("/api/participants").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testParticipant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testParticipant.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipant() throws Exception {
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Create the Participant
        ParticipantDTO participantDTO = participantMapper.toDto(participant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipantMockMvc.perform(put("/api/participants").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Delete the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
