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

import com.gestionevenement.domain.InscriptionEvenement;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.InscriptionEvenementRepository;
import com.gestionevenement.service.dto.InscriptionEvenementCriteria;
import com.gestionevenement.service.dto.InscriptionEvenementDTO;
import com.gestionevenement.service.mapper.InscriptionEvenementMapper;

/**
 * Service for executing complex queries for {@link InscriptionEvenement} entities in the database.
 * The main input is a {@link InscriptionEvenementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InscriptionEvenementDTO} or a {@link Page} of {@link InscriptionEvenementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InscriptionEvenementQueryService extends QueryService<InscriptionEvenement> {

    private final Logger log = LoggerFactory.getLogger(InscriptionEvenementQueryService.class);

    private final InscriptionEvenementRepository inscriptionEvenementRepository;

    private final InscriptionEvenementMapper inscriptionEvenementMapper;

    public InscriptionEvenementQueryService(InscriptionEvenementRepository inscriptionEvenementRepository, InscriptionEvenementMapper inscriptionEvenementMapper) {
        this.inscriptionEvenementRepository = inscriptionEvenementRepository;
        this.inscriptionEvenementMapper = inscriptionEvenementMapper;
    }

    /**
     * Return a {@link List} of {@link InscriptionEvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InscriptionEvenementDTO> findByCriteria(InscriptionEvenementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InscriptionEvenement> specification = createSpecification(criteria);
        return inscriptionEvenementMapper.toDto(inscriptionEvenementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InscriptionEvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InscriptionEvenementDTO> findByCriteria(InscriptionEvenementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InscriptionEvenement> specification = createSpecification(criteria);
        return inscriptionEvenementRepository.findAll(specification, page)
            .map(inscriptionEvenementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InscriptionEvenementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InscriptionEvenement> specification = createSpecification(criteria);
        return inscriptionEvenementRepository.count(specification);
    }

    /**
     * Function to convert {@link InscriptionEvenementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InscriptionEvenement> createSpecification(InscriptionEvenementCriteria criteria) {
        Specification<InscriptionEvenement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InscriptionEvenement_.id));
            }
            if (criteria.getLoginParticipant() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLoginParticipant(), InscriptionEvenement_.loginParticipant));
            }
            if (criteria.getPasswordParticipant() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPasswordParticipant(), InscriptionEvenement_.passwordParticipant));
            }
            if (criteria.getEvenementId() != null) {
                specification = specification.and(buildSpecification(criteria.getEvenementId(),
                    root -> root.join(InscriptionEvenement_.evenement, JoinType.LEFT).get(Evenement_.id)));
            }
            if (criteria.getParticipantId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantId(),
                    root -> root.join(InscriptionEvenement_.participant, JoinType.LEFT).get(Participant_.id)));
            }
        }
        return specification;
    }
}
