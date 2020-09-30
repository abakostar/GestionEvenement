package com.gestionevenement.service.impl;

import com.gestionevenement.service.InscriptionActiviteService;
import com.gestionevenement.domain.InscriptionActivite;
import com.gestionevenement.repository.InscriptionActiviteRepository;
import com.gestionevenement.service.dto.InscriptionActiviteDTO;
import com.gestionevenement.service.mapper.InscriptionActiviteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InscriptionActivite}.
 */
@Service
@Transactional
public class InscriptionActiviteServiceImpl implements InscriptionActiviteService {

    private final Logger log = LoggerFactory.getLogger(InscriptionActiviteServiceImpl.class);

    private final InscriptionActiviteRepository inscriptionActiviteRepository;

    private final InscriptionActiviteMapper inscriptionActiviteMapper;

    public InscriptionActiviteServiceImpl(InscriptionActiviteRepository inscriptionActiviteRepository, InscriptionActiviteMapper inscriptionActiviteMapper) {
        this.inscriptionActiviteRepository = inscriptionActiviteRepository;
        this.inscriptionActiviteMapper = inscriptionActiviteMapper;
    }

    @Override
    public InscriptionActiviteDTO save(InscriptionActiviteDTO inscriptionActiviteDTO) {
        log.debug("Request to save InscriptionActivite : {}", inscriptionActiviteDTO);
        InscriptionActivite inscriptionActivite = inscriptionActiviteMapper.toEntity(inscriptionActiviteDTO);
        inscriptionActivite = inscriptionActiviteRepository.save(inscriptionActivite);
        return inscriptionActiviteMapper.toDto(inscriptionActivite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionActiviteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InscriptionActivites");
        return inscriptionActiviteRepository.findAll(pageable)
            .map(inscriptionActiviteMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionActiviteDTO> findOne(Long id) {
        log.debug("Request to get InscriptionActivite : {}", id);
        return inscriptionActiviteRepository.findById(id)
            .map(inscriptionActiviteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InscriptionActivite : {}", id);
        inscriptionActiviteRepository.deleteById(id);
    }
}
