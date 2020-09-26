package com.gestionevenement.repository;

import com.gestionevenement.domain.Participant;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Participant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

    @Query("select participant from Participant participant where participant.user.login = ?#{principal.username}")
    List<Participant> findByUserIsCurrentUser();
}
