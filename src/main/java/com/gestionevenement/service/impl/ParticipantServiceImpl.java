package com.gestionevenement.service.impl;

import com.gestionevenement.domain.*;
import com.gestionevenement.repository.ActiviteRepository;
import com.gestionevenement.repository.ParticipantActiviteRepository;
import com.gestionevenement.service.EvenementQueryService;
import com.gestionevenement.service.ParticipantService;
import com.gestionevenement.repository.ParticipantEvenementRepository;
import com.gestionevenement.repository.ParticipantRepository;
import com.gestionevenement.service.dto.*;
import com.gestionevenement.service.mapper.ActiviteMapper;
import com.gestionevenement.service.mapper.ParticipantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Participant}.
 */
@Service
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

    private final Logger log = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    private final ParticipantRepository participantRepository;

    private final ParticipantMapper participantMapper;

    private final ParticipantEvenementRepository participantEvenementRepository;

    private final EvenementQueryService evenementQueryService;

    private final ActiviteRepository activiteRepository;

    private final ParticipantActiviteRepository participantActiviteRepository;

    private final ActiviteMapper activiteMapper;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, ParticipantMapper participantMapper,
                                  ParticipantEvenementRepository participantEvenementRepository,
                                  ActiviteRepository activiteRepository,
                                  ParticipantActiviteRepository participantActiviteRepository,
                                  ActiviteMapper activiteMapper,
                                  EvenementQueryService evenementQueryService) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.participantEvenementRepository = participantEvenementRepository;
        this.activiteRepository = activiteRepository;
        this.participantActiviteRepository = participantActiviteRepository;
        this.evenementQueryService = evenementQueryService;
        this.activiteMapper = activiteMapper;
    }

    @Override
    public ParticipantDTO save(ParticipantDTO participantDTO) {
        log.debug("Request to save Participant : {}", participantDTO);
        Participant participant = participantMapper.toEntity(participantDTO);
        participant = participantRepository.save(participant);
        return participantMapper.toDto(participant);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ParticipantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participants");
        return participantRepository.findAll(pageable)
            .map(participantMapper::toDto);
    }


    public Page<ParticipantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return participantRepository.findAllWithEagerRelationships(pageable).map(participantMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipantDTO> findOne(Long id) {
        log.debug("Request to get Participant : {}", id);
        return participantRepository.findOneWithEagerRelationships(id)
            .map(participantMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participant : {}", id);
        participantRepository.deleteById(id);
    }

    @Override
    public Optional<ParticipantDTO> findByUser(User user) {
        Participant participant = participantRepository.findOneByLoginWithEagerRelationships(user.getLogin()).orElse(null);
        ParticipantDTO participantDTO;
        if (participant == null) {
            participantDTO = new ParticipantDTO();
            participantDTO.setProfileCompleted(false);
        } else {
            participantDTO = participantMapper.toDto(participant);
            participantDTO.setProfileCompleted(true);
        }
        participantDTO.setUser(user);
        participantDTO.setEvenements(buildEvenementDtos(participant));
        return Optional.of(participantDTO);
    }

    @Override
    public Optional<List<ParticipantActiviteDTO>> findAllParticipantActiviteByUser(User user, Long evenementId) {
        Participant participant = participantRepository.findOneByLoginWithEagerRelationships(user.getLogin()).orElse(null);
        if (participant == null) {
            return Optional.of(Collections.emptyList());
        }

        List<Activite> activites = activiteRepository.findAllActiviteByEvenementId(evenementId);
        if (activites == null || activites.size() == 0) {
            return Optional.of(Collections.emptyList());
        }

        Map<Long, ParticipantActivite> participantActivitesMap = new HashMap<>();
        List<ParticipantActivite> participantActivites = participantActiviteRepository.findAllByParticipantId(participant.getId());
        participantActivites.forEach(participantActivite -> participantActivitesMap.put(participantActivite.getActiviteId(), participantActivite));

        List<ParticipantActiviteDTO> results = activites.stream().map(activite -> {
            ParticipantActivite participantActivite = participantActivitesMap.get(activite.getId());
            ParticipantActiviteDTO dto = new ParticipantActiviteDTO();
            dto.setActivite(activiteMapper.toDto(activite));
            dto.setRole(participantActivite == null ? null : participantActivite.getRole());
            dto.setRegistered(participantActivite != null);
            return dto;
        }).collect(Collectors.toList());

        return Optional.of(results);
    }

    private List<EvenementDTO> buildEvenementDtos(Participant participant) {
        if (participant == null) {
            return Collections.emptyList();
        }
        List<ParticipantEvenement> allByParticipantId = participantEvenementRepository.findAllByParticipantId(participant.getId());
        Set<Long> participantEvenementIds = (allByParticipantId == null || allByParticipantId.size() == 0)
            ? Collections.emptySet()
            : allByParticipantId.stream().map(ParticipantEvenement::getEvenementId).collect(Collectors.toSet());
        List<EvenementDTO> allEvenements = evenementQueryService.findByCriteria(null);
        if (allEvenements == null || allEvenements.size() == 0) {
            return Collections.emptyList();
        }
        allEvenements.forEach(evenementDTO -> evenementDTO.setRegistered(participantEvenementIds.contains(evenementDTO.getId())));
        return allEvenements;
    }

}
