package com.gestionevenement.repository;

import com.gestionevenement.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantEvenementRepository extends JpaRepository<ParticipantEvenement, ParticipantEvenemrntId>, JpaSpecificationExecutor<ParticipantEvenement> {

    List<ParticipantEvenement> findAllByEvenementId(Long evenementId);
}
