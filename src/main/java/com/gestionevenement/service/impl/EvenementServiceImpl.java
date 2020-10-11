package com.gestionevenement.service.impl;

import com.gestionevenement.domain.ParticipantEvenement;
import com.gestionevenement.repository.ParticipantEvenementRepository;
import com.gestionevenement.service.ActiviteQueryService;
import com.gestionevenement.service.EvenementService;
import com.gestionevenement.domain.Evenement;
import com.gestionevenement.repository.EvenementRepository;
import com.gestionevenement.service.ParticipantQueryService;
import com.gestionevenement.service.dto.*;
import com.gestionevenement.service.mapper.EvenementMapper;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing {@link Evenement}.
 */
@Service
@Transactional
public class EvenementServiceImpl implements EvenementService {

    private final Logger log = LoggerFactory.getLogger(EvenementServiceImpl.class);

    private final EvenementRepository evenementRepository;

    private final EvenementMapper evenementMapper;

    private final ParticipantEvenementRepository participantEvenementRepository;

    private final ParticipantQueryService participantQueryService;

    private final ActiviteQueryService activiteQueryService;

    public EvenementServiceImpl(EvenementRepository evenementRepository,
                                ParticipantEvenementRepository participantEvenementRepository,
                                ParticipantQueryService participantQueryService,
                                EvenementMapper evenementMapper,
                                ActiviteQueryService activiteQueryService) {
        this.evenementRepository = evenementRepository;
        this.evenementMapper = evenementMapper;
        this.participantEvenementRepository = participantEvenementRepository;
        this.participantQueryService = participantQueryService;
        this.activiteQueryService = activiteQueryService;
    }

    @Override
    public EvenementDTO save(EvenementDTO evenementDTO) {
        log.debug("Request to save Evenement : {}", evenementDTO);
        Evenement evenement = evenementMapper.toEntity(evenementDTO);
        evenement = evenementRepository.save(evenement);
        return evenementMapper.toDto(evenement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EvenementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Evenements");
        return evenementRepository.findAll(pageable)
            .map(evenementMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EvenementDTO> findOne(Long id) {
        log.debug("Request to get Evenement : {}", id);
        return evenementRepository.findById(id)
            .map(evenement -> {
                EvenementDTO evenementDTO = evenementMapper.toDto(evenement);
                evenementDTO.setParticipants(computeParticipantEvent(evenementDTO.getId()));
                evenementDTO.setActivites(computeActiviteEvent(evenementDTO.getId()));
                return evenementDTO;
            });
    }

    private List<ActiviteDTO> computeActiviteEvent(Long id) {
        ActiviteCriteria activiteCriteria  = new ActiviteCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(id);
        activiteCriteria.setEvenementId(longFilter);
        return activiteQueryService.findByCriteria(activiteCriteria);
    }




        private List<ParticipantEventDTO> computeParticipantEvent(Long id) {
        Map<Long, ParticipantEventDTO> map = new HashMap<>();
        List<ParticipantDTO> participantDTOS = participantQueryService.findByCriteria(null);
        if (participantDTOS == null || participantDTOS.size() == 0) {
            return new ArrayList<>();
        } else {
            participantDTOS.forEach(participantDTO -> map.put(participantDTO.getId(), new ParticipantEventDTO(participantDTO, false)));
        }

        List<ParticipantEvenement> evenements = participantEvenementRepository.findAllByEvenementId(id);
        if (evenements != null && evenements.size() > 0) {
            evenements.forEach(participantEvenement -> {
                ParticipantEventDTO participantEventDTO = map.get(participantEvenement.getParticipantId());
                if(participantEventDTO != null){
                    participantEventDTO.setRegistered(true);
                }
            });
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evenement : {}", id);
        evenementRepository.deleteById(id);
    }
}
