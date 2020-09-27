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

import com.gestionevenement.domain.Inscription;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.InscriptionRepository;
import com.gestionevenement.service.dto.InscriptionCriteria;
import com.gestionevenement.service.dto.InscriptionDTO;
import com.gestionevenement.service.mapper.InscriptionMapper;

/**
 * Service for executing complex queries for {@link Inscription} entities in the database.
 * The main input is a {@link InscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InscriptionDTO} or a {@link Page} of {@link InscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InscriptionQueryService extends QueryService<Inscription> {

    private final Logger log = LoggerFactory.getLogger(InscriptionQueryService.class);

    private final InscriptionRepository inscriptionRepository;

    private final InscriptionMapper inscriptionMapper;

    public InscriptionQueryService(InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
    }

    /**
     * Return a {@link List} of {@link InscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InscriptionDTO> findByCriteria(InscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inscription> specification = createSpecification(criteria);
        return inscriptionMapper.toDto(inscriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InscriptionDTO> findByCriteria(InscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inscription> specification = createSpecification(criteria);
        return inscriptionRepository.findAll(specification, page)
            .map(inscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inscription> specification = createSpecification(criteria);
        return inscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link InscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Inscription> createSpecification(InscriptionCriteria criteria) {
        Specification<Inscription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Inscription_.id));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), Inscription_.role));
            }
            if (criteria.getActiviteId() != null) {
                specification = specification.and(buildSpecification(criteria.getActiviteId(),
                    root -> root.join(Inscription_.activite, JoinType.LEFT).get(Activite_.id)));
            }
            if (criteria.getParticipantId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantId(),
                    root -> root.join(Inscription_.participant, JoinType.LEFT).get(Participant_.id)));
            }
        }
        return specification;
    }
}
