package com.gestionevenement.repository;

import com.gestionevenement.domain.Ville;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ville entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VilleRepository extends JpaRepository<Ville, Long>, JpaSpecificationExecutor<Ville> {
}
