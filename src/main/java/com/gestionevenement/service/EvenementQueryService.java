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

import com.gestionevenement.domain.Evenement;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.EvenementRepository;
import com.gestionevenement.service.dto.EvenementCriteria;
import com.gestionevenement.service.dto.EvenementDTO;
import com.gestionevenement.service.mapper.EvenementMapper;

/**
 * Service for executing complex queries for {@link Evenement} entities in the database.
 * The main input is a {@link EvenementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EvenementDTO} or a {@link Page} of {@link EvenementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvenementQueryService extends QueryService<Evenement> {

    private final Logger log = LoggerFactory.getLogger(EvenementQueryService.class);

    private final EvenementRepository evenementRepository;

    private final EvenementMapper evenementMapper;

    public EvenementQueryService(EvenementRepository evenementRepository, EvenementMapper evenementMapper) {
        this.evenementRepository = evenementRepository;
        this.evenementMapper = evenementMapper;
    }

    /**
     * Return a {@link List} of {@link EvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EvenementDTO> findByCriteria(EvenementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementMapper.toDto(evenementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EvenementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvenementDTO> findByCriteria(EvenementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementRepository.findAll(specification, page)
            .map(evenementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvenementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evenement> specification = createSpecification(criteria);
        return evenementRepository.count(specification);
    }

    /**
     * Function to convert {@link EvenementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evenement> createSpecification(EvenementCriteria criteria) {
        Specification<Evenement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evenement_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Evenement_.code));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Evenement_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Evenement_.dateFin));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Evenement_.description));
            }
            if (criteria.getCategorieId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategorieId(),
                    root -> root.join(Evenement_.categorie, JoinType.LEFT).get(Categorie_.id)));
            }
        }
        return specification;
    }
}
