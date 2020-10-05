package com.gestionevenement.repository;

import com.gestionevenement.domain.Activite;
import com.gestionevenement.domain.ParticipantActivite;
import com.gestionevenement.domain.ParticipantActiviteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantActiviteRepository extends JpaRepository<ParticipantActivite, ParticipantActiviteId>, JpaSpecificationExecutor<Activite> {
}
