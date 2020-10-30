package com.gestionevenement.service.impl;

import com.gestionevenement.domain.ParticipantActivite;
import com.gestionevenement.repository.ParticipantActiviteRepository;
import com.gestionevenement.service.ActiviteService;
import com.gestionevenement.domain.Activite;
import com.gestionevenement.repository.ActiviteRepository;
import com.gestionevenement.service.EmplacementService;
import com.gestionevenement.service.ParticipantQueryService;
import com.gestionevenement.service.dto.*;
import com.gestionevenement.service.mapper.ActiviteMapper;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing {@link Activite}.
 */
@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService {

    private final Logger log = LoggerFactory.getLogger(ActiviteServiceImpl.class);

    private final ActiviteRepository activiteRepository;

    private final ActiviteMapper activiteMapper;
    private final ParticipantActiviteRepository participantActiviteRepository;
    private final ParticipantQueryService participantQueryService;
    private  final EmplacementService emplacementService;


    public ActiviteServiceImpl(ActiviteRepository activiteRepository, ActiviteMapper activiteMapper,
                               ParticipantQueryService participantQueryService,
                               ParticipantActiviteRepository participantActiviteRepository, EmplacementService emplacementService) {
        this.activiteRepository = activiteRepository;
        this.activiteMapper = activiteMapper;
        this.participantQueryService = participantQueryService;
        this.participantActiviteRepository = participantActiviteRepository;
        this.emplacementService = emplacementService;
    }

    @Override
    public ActiviteDTO save(ActiviteDTO activiteDTO) {
        log.debug("Request to save Activite : {}", activiteDTO);
        Activite activite = activiteMapper.toEntity(activiteDTO);
        activite = activiteRepository.save(activite);
        return activiteMapper.toDto(activite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActiviteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Activites");
        return activiteRepository.findAll(pageable)
            .map(activiteMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ActiviteDTO> findOne(Long id) {
        log.debug("Request to get Activite : {}", id);
        return activiteRepository.findById(id)
            //.map(activiteMapper::toDto);
        .map(activite -> {
            ActiviteDTO activiteDTO = activiteMapper.toDto(activite);
            activiteDTO.setParticipants(ajoutParticipantActivite(activiteDTO.getId()));
            return  activiteDTO;
        });
    }

    // Ajout AS

    public Boolean placedispo(Long id){
        EmplacementDTO emplacementDTO = new EmplacementDTO();
        EmplacementCriteria emplacementCriteria = new EmplacementCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(id);
        emplacementCriteria.setId(longFilter);
        emplacementDTO = emplacementService.findOne(id).orElse(null);
        Integer nbparticipant = ((int) participantActiviteRepository.findAllByActiviteId(id).stream().count());
        if (emplacementDTO.getCapacite() !=null && nbparticipant != 0){
          if   (emplacementDTO.getCapacite() > nbparticipant ){
              log.debug("Request to test of local available :  Place disponible {}", id);
              return true;
          }
        }
        return  false;
    }


    private List<ParticipantActiviteDTO> ajoutParticipantActivite(Long id) {
        Map<Long, ParticipantActiviteDTO> map = new HashMap<>();
        List<ParticipantDTO> participantsDTO = participantQueryService.findByCriteria(null);
        if (participantsDTO == null || participantsDTO.size() == 0) {
            return new ArrayList<>();
        } else {
            participantsDTO.forEach(participantDTO -> map.put(participantDTO.getId(), new ParticipantActiviteDTO(participantDTO, "participant",false)));
        }
        List<ParticipantActivite> activites = participantActiviteRepository.findAllByActiviteId(id);
        if (activites != null && activites.size() > 0) {
            activites.forEach( participantActivite -> {
                    ParticipantActiviteDTO participantActiviteDTO = map.get(participantActivite.getParticipantId());
                    if(participantActiviteDTO !=null){
                        participantActiviteDTO.setRole(participantActivite.getRole());
                        participantActiviteDTO.setRegistered(true);
                    }
                }
            );
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.deleteById(id);
    }
}
