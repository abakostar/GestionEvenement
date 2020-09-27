package com.gestionevenement.repository;

import com.gestionevenement.domain.Inscription;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Inscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long>, JpaSpecificationExecutor<Inscription> {
}
