package com.gestionevenement.repository;

import com.gestionevenement.domain.Participant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Participant entity.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

    @Query(value = "select distinct participant from Participant participant left join fetch participant.evenements left join fetch participant.activites",
        countQuery = "select count(distinct participant) from Participant participant")
    Page<Participant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct participant from Participant participant left join fetch participant.evenements left join fetch participant.activites")
    List<Participant> findAllWithEagerRelationships();

    @Query("select participant from Participant participant left join fetch participant.evenements left join fetch participant.activites where participant.id =:id")
    Optional<Participant> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select participant from Participant participant where participant.login =:login")
    Optional<Participant> findOneByLoginWithEagerRelationships(@Param("login") String login);
}
