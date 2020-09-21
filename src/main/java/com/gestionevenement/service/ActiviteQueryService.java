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

import com.gestionevenement.domain.Activite;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.ActiviteRepository;
import com.gestionevenement.service.dto.ActiviteCriteria;
import com.gestionevenement.service.dto.ActiviteDTO;
import com.gestionevenement.service.mapper.ActiviteMapper;

/**
 * Service for executing complex queries for {@link Activite} entities in the database.
 * The main input is a {@link ActiviteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActiviteDTO} or a {@link Page} of {@link ActiviteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActiviteQueryService extends QueryService<Activite> {

    private final Logger log = LoggerFactory.getLogger(ActiviteQueryService.class);

    private final ActiviteRepository activiteRepository;

    private final ActiviteMapper activiteMapper;

    public ActiviteQueryService(ActiviteRepository activiteRepository, ActiviteMapper activiteMapper) {
        this.activiteRepository = activiteRepository;
        this.activiteMapper = activiteMapper;
    }

    /**
     * Return a {@link List} of {@link ActiviteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActiviteDTO> findByCriteria(ActiviteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteMapper.toDto(activiteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActiviteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActiviteDTO> findByCriteria(ActiviteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteRepository.findAll(specification, page)
            .map(activiteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActiviteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteRepository.count(specification);
    }

    /**
     * Function to convert {@link ActiviteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Activite> createSpecification(ActiviteCriteria criteria) {
        Specification<Activite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Activite_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Activite_.nom));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Activite_.description));
            }
            if (criteria.getDate_activite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate_activite(), Activite_.date_activite));
            }
            if (criteria.getHeure_debut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeure_debut(), Activite_.heure_debut));
            }
            if (criteria.getHeure_fin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeure_fin(), Activite_.heure_fin));
            }
            if (criteria.getEtatclos() != null) {
                specification = specification.and(buildSpecification(criteria.getEtatclos(), Activite_.etatclos));
            }
            if (criteria.getEvenementId() != null) {
                specification = specification.and(buildSpecification(criteria.getEvenementId(),
                    root -> root.join(Activite_.evenement, JoinType.LEFT).get(Evenement_.id)));
            }
            if (criteria.getEmplacementId() != null) {
                specification = specification.and(buildSpecification(criteria.getEmplacementId(),
                    root -> root.join(Activite_.emplacement, JoinType.LEFT).get(Emplacement_.id)));
            }
        }
        return specification;
    }
}
