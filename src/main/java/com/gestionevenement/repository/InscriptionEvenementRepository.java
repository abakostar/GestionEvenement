package com.gestionevenement.repository;

import com.gestionevenement.domain.InscriptionEvenement;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InscriptionEvenement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionEvenementRepository extends JpaRepository<InscriptionEvenement, Long>, JpaSpecificationExecutor<InscriptionEvenement> {
}
