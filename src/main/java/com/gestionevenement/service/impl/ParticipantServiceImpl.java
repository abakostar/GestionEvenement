package com.gestionevenement.service.impl;

import com.gestionevenement.service.EvenementQueryService;
import com.gestionevenement.service.ParticipantService;
import com.gestionevenement.domain.Participant;
import com.gestionevenement.domain.ParticipantEvenement;
import com.gestionevenement.repository.ParticipantEvenementRepository;
import com.gestionevenement.repository.ParticipantRepository;
import com.gestionevenement.service.dto.*;
import com.gestionevenement.service.mapper.ParticipantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public ParticipantServiceImpl(ParticipantRepository participantRepository, ParticipantMapper participantMapper,
            ParticipantEvenementRepository participantEvenementRepository,
            EvenementQueryService evenementQueryService) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.participantEvenementRepository = participantEvenementRepository;
        this.evenementQueryService = evenementQueryService;
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
            //.map(participantMapper::toDto);
            .map(participant ->{
                ParticipantDTO participantDTO = participantMapper.toDto(participant);
                participantDTO.setParticipantEvenements(registerParticipantEvent(participantDTO.getId()));
                return participantDTO;
            });
    }

    private List<ParticipantEventDTO> registerParticipantEvent(Long id){
        Map<Long, ParticipantEventDTO> map = new HashMap<>();
        List<EvenementDTO> evenementDTOs = evenementQueryService.findByCriteria(null);
        if (evenementDTOs == null || evenementDTOs.size() == 0){
            return new ArrayList<>();
        }else{
            evenementDTOs.forEach(evenementparticipant -> map.put(evenementparticipant.getId(), new ParticipantEventDTO(evenementparticipant, false)));
        }

        List<ParticipantEvenement> participants = participantEvenementRepository.findAllByParticipantId(id);
        if (participants != null && participants.size() > 0) {
            participants.forEach(participantEvenement -> {
                ParticipantEventDTO participantEventDTO = map.get(participantEvenement.getEvenementId());
                if(participantEventDTO != null){
                    participantEventDTO.setRegistered(true);
                }
            });
        }
            return new ArrayList<>(map.values());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participant : {}", id);
        participantRepository.deleteById(id);
    }

}
