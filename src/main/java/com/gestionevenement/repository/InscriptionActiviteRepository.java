package com.gestionevenement.repository;

import com.gestionevenement.domain.InscriptionActivite;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InscriptionActivite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionActiviteRepository extends JpaRepository<InscriptionActivite, Long>, JpaSpecificationExecutor<InscriptionActivite> {
}
