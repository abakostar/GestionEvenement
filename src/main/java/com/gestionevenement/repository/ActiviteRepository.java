package com.gestionevenement.repository;

import com.gestionevenement.domain.Activite;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Activite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long>, JpaSpecificationExecutor<Activite> {
}
