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

import com.gestionevenement.domain.Ville;
import com.gestionevenement.domain.*; // for static metamodels
import com.gestionevenement.repository.VilleRepository;
import com.gestionevenement.service.dto.VilleCriteria;
import com.gestionevenement.service.dto.VilleDTO;
import com.gestionevenement.service.mapper.VilleMapper;

/**
 * Service for executing complex queries for {@link Ville} entities in the database.
 * The main input is a {@link VilleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VilleDTO} or a {@link Page} of {@link VilleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VilleQueryService extends QueryService<Ville> {

    private final Logger log = LoggerFactory.getLogger(VilleQueryService.class);

    private final VilleRepository villeRepository;

    private final VilleMapper villeMapper;

    public VilleQueryService(VilleRepository villeRepository, VilleMapper villeMapper) {
        this.villeRepository = villeRepository;
        this.villeMapper = villeMapper;
    }

    /**
     * Return a {@link List} of {@link VilleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VilleDTO> findByCriteria(VilleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ville> specification = createSpecification(criteria);
        return villeMapper.toDto(villeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VilleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VilleDTO> findByCriteria(VilleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ville> specification = createSpecification(criteria);
        return villeRepository.findAll(specification, page)
            .map(villeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VilleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ville> specification = createSpecification(criteria);
        return villeRepository.count(specification);
    }

    /**
     * Function to convert {@link VilleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ville> createSpecification(VilleCriteria criteria) {
        Specification<Ville> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ville_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Ville_.nom));
            }
            if (criteria.getPaysId() != null) {
                specification = specification.and(buildSpecification(criteria.getPaysId(),
                    root -> root.join(Ville_.pays, JoinType.LEFT).get(Pays_.id)));
            }
        }
        return specification;
    }
}
