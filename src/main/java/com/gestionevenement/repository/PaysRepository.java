package com.gestionevenement.repository;

import com.gestionevenement.domain.Pays;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Pays entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaysRepository extends JpaRepository<Pays, Long>, JpaSpecificationExecutor<Pays> {
}
