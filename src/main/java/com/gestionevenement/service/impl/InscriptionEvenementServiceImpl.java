package com.gestionevenement.service.impl;

import com.gestionevenement.service.InscriptionEvenementService;
import com.gestionevenement.domain.InscriptionEvenement;
import com.gestionevenement.repository.InscriptionEvenementRepository;
import com.gestionevenement.service.dto.InscriptionEvenementDTO;
import com.gestionevenement.service.mapper.InscriptionEvenementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InscriptionEvenement}.
 */
@Service
@Transactional
public class InscriptionEvenementServiceImpl implements InscriptionEvenementService {

    private final Logger log = LoggerFactory.getLogger(InscriptionEvenementServiceImpl.class);

    private final InscriptionEvenementRepository inscriptionEvenementRepository;

    private final InscriptionEvenementMapper inscriptionEvenementMapper;

    public InscriptionEvenementServiceImpl(InscriptionEvenementRepository inscriptionEvenementRepository, InscriptionEvenementMapper inscriptionEvenementMapper) {
        this.inscriptionEvenementRepository = inscriptionEvenementRepository;
        this.inscriptionEvenementMapper = inscriptionEvenementMapper;
    }

    @Override
    public InscriptionEvenementDTO save(InscriptionEvenementDTO inscriptionEvenementDTO) {
        log.debug("Request to save InscriptionEvenement : {}", inscriptionEvenementDTO);
        InscriptionEvenement inscriptionEvenement = inscriptionEvenementMapper.toEntity(inscriptionEvenementDTO);
        inscriptionEvenement = inscriptionEvenementRepository.save(inscriptionEvenement);
        return inscriptionEvenementMapper.toDto(inscriptionEvenement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionEvenementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InscriptionEvenements");
        return inscriptionEvenementRepository.findAll(pageable)
            .map(inscriptionEvenementMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionEvenementDTO> findOne(Long id) {
        log.debug("Request to get InscriptionEvenement : {}", id);
        return inscriptionEvenementRepository.findById(id)
            .map(inscriptionEvenementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InscriptionEvenement : {}", id);
        inscriptionEvenementRepository.deleteById(id);
    }
}
