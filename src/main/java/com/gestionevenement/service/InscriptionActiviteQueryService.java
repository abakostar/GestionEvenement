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

import com.gestionevenement.domain.InscriptionActivite;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.InscriptionActiviteRepository;
import com.gestionevenement.service.dto.InscriptionActiviteCriteria;
import com.gestionevenement.service.dto.InscriptionActiviteDTO;
import com.gestionevenement.service.mapper.InscriptionActiviteMapper;

/**
 * Service for executing complex queries for {@link InscriptionActivite} entities in the database.
 * The main input is a {@link InscriptionActiviteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InscriptionActiviteDTO} or a {@link Page} of {@link InscriptionActiviteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InscriptionActiviteQueryService extends QueryService<InscriptionActivite> {

    private final Logger log = LoggerFactory.getLogger(InscriptionActiviteQueryService.class);

    private final InscriptionActiviteRepository inscriptionActiviteRepository;

    private final InscriptionActiviteMapper inscriptionActiviteMapper;

    public InscriptionActiviteQueryService(InscriptionActiviteRepository inscriptionActiviteRepository, InscriptionActiviteMapper inscriptionActiviteMapper) {
        this.inscriptionActiviteRepository = inscriptionActiviteRepository;
        this.inscriptionActiviteMapper = inscriptionActiviteMapper;
    }

    /**
     * Return a {@link List} of {@link InscriptionActiviteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InscriptionActiviteDTO> findByCriteria(InscriptionActiviteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InscriptionActivite> specification = createSpecification(criteria);
        return inscriptionActiviteMapper.toDto(inscriptionActiviteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InscriptionActiviteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InscriptionActiviteDTO> findByCriteria(InscriptionActiviteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InscriptionActivite> specification = createSpecification(criteria);
        return inscriptionActiviteRepository.findAll(specification, page)
            .map(inscriptionActiviteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InscriptionActiviteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InscriptionActivite> specification = createSpecification(criteria);
        return inscriptionActiviteRepository.count(specification);
    }

    /**
     * Function to convert {@link InscriptionActiviteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InscriptionActivite> createSpecification(InscriptionActiviteCriteria criteria) {
        Specification<InscriptionActivite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InscriptionActivite_.id));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), InscriptionActivite_.role));
            }
            if (criteria.getActiviteId() != null) {
                specification = specification.and(buildSpecification(criteria.getActiviteId(),
                    root -> root.join(InscriptionActivite_.activite, JoinType.LEFT).get(Activite_.id)));
            }
            if (criteria.getParticipantId() != null) {
                specification = specification.and(buildSpecification(criteria.getParticipantId(),
                    root -> root.join(InscriptionActivite_.participant, JoinType.LEFT).get(Participant_.id)));
            }
        }
        return specification;
    }
}
