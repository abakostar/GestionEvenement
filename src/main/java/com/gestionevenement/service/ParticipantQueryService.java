package com.gestionevenement.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.gestionevenement.domain.Participant;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.ParticipantRepository;
import com.gestionevenement.service.dto.ParticipantCriteria;
import com.gestionevenement.service.dto.ParticipantDTO;
import com.gestionevenement.service.mapper.ParticipantMapper;

/**
 * Service for executing complex queries for {@link Participant} entities in the database.
 * The main input is a {@link ParticipantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParticipantDTO} or a {@link Page} of {@link ParticipantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParticipantQueryService extends QueryService<Participant> {

    private final Logger log = LoggerFactory.getLogger(ParticipantQueryService.class);

    private final ParticipantRepository participantRepository;

    private final ParticipantMapper participantMapper;

    private final UserService userService;

    public ParticipantQueryService(ParticipantRepository participantRepository, UserService userService, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link ParticipantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParticipantDTO> findByCriteria(ParticipantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Participant> specification = createSpecification(criteria);
        List<ParticipantDTO> participantDTOS = participantMapper.toDto(participantRepository.findAll(specification));
        participantDTOS.forEach(participantDTO -> {
            User user = userService.findByLogin(participantDTO.getLogin());
            if (user != null) participantDTO.setUser(user);
        });
        return participantDTOS;
    }

    /**
     * Return a {@link Page} of {@link ParticipantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipantDTO> findByCriteria(ParticipantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Participant> specification = createSpecification(criteria);
        Page<ParticipantDTO> pageResult = participantRepository.findAll(specification, page).map(participantMapper::toDto);
        pageResult.forEach(participantDTO -> {
            User user = userService.findByLogin(participantDTO.getLogin());
            if (user != null) participantDTO.setUser(user);
        });
        return pageResult;
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParticipantCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Participant> specification = createSpecification(criteria);
        return participantRepository.count(specification);
    }

    /**
     * Function to convert {@link ParticipantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Participant> createSpecification(ParticipantCriteria criteria) {
        Specification<Participant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Participant_.id));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSexe(), Participant_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Participant_.telephone));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), Participant_.login));
            }
            if (criteria.getVilleId() != null) {
                specification = specification.and(buildSpecification(criteria.getVilleId(),
                    root -> root.join(Participant_.ville, JoinType.LEFT).get(Ville_.id)));
            }
            if (criteria.getEvenementId() != null) {
                specification = specification.and(buildSpecification(criteria.getEvenementId(),
                    root -> root.join(Participant_.evenements, JoinType.LEFT).get(Evenement_.id)));
            }
            if (criteria.getActiviteId() != null) {
                specification = specification.and(buildSpecification(criteria.getActiviteId(),
                    root -> root.join(Participant_.activites, JoinType.LEFT).get(Activite_.id)));
            }
        }
        return specification;
    }
}
