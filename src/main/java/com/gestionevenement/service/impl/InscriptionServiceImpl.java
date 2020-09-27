package com.gestionevenement.service.impl;

import com.gestionevenement.service.InscriptionService;
import com.gestionevenement.domain.Inscription;
import com.gestionevenement.repository.InscriptionRepository;
import com.gestionevenement.service.dto.InscriptionDTO;
import com.gestionevenement.service.mapper.InscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    private final InscriptionMapper inscriptionMapper;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
    }

    @Override
    public InscriptionDTO save(InscriptionDTO inscriptionDTO) {
        log.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable)
            .map(inscriptionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionDTO> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id)
            .map(inscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }
}
