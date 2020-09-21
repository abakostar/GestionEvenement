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

import com.gestionevenement.domain.Emplacement;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.EmplacementRepository;
import com.gestionevenement.service.dto.EmplacementCriteria;
import com.gestionevenement.service.dto.EmplacementDTO;
import com.gestionevenement.service.mapper.EmplacementMapper;

/**
 * Service for executing complex queries for {@link Emplacement} entities in the database.
 * The main input is a {@link EmplacementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmplacementDTO} or a {@link Page} of {@link EmplacementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmplacementQueryService extends QueryService<Emplacement> {

    private final Logger log = LoggerFactory.getLogger(EmplacementQueryService.class);

    private final EmplacementRepository emplacementRepository;

    private final EmplacementMapper emplacementMapper;

    public EmplacementQueryService(EmplacementRepository emplacementRepository, EmplacementMapper emplacementMapper) {
        this.emplacementRepository = emplacementRepository;
        this.emplacementMapper = emplacementMapper;
    }

    /**
     * Return a {@link List} of {@link EmplacementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmplacementDTO> findByCriteria(EmplacementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Emplacement> specification = createSpecification(criteria);
        return emplacementMapper.toDto(emplacementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmplacementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmplacementDTO> findByCriteria(EmplacementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Emplacement> specification = createSpecification(criteria);
        return emplacementRepository.findAll(specification, page)
            .map(emplacementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmplacementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Emplacement> specification = createSpecification(criteria);
        return emplacementRepository.count(specification);
    }

    /**
     * Function to convert {@link EmplacementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Emplacement> createSpecification(EmplacementCriteria criteria) {
        Specification<Emplacement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Emplacement_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Emplacement_.code));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Emplacement_.description));
            }
            if (criteria.getCapacite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacite(), Emplacement_.capacite));
            }
            if (criteria.getVilleId() != null) {
                specification = specification.and(buildSpecification(criteria.getVilleId(),
                    root -> root.join(Emplacement_.ville, JoinType.LEFT).get(Ville_.id)));
            }
        }
        return specification;
    }
}
