package com.gestionevenement.service.impl;

import com.gestionevenement.service.EvenementService;
import com.gestionevenement.domain.Evenement;
import com.gestionevenement.repository.EvenementRepository;
import com.gestionevenement.service.dto.EvenementDTO;
import com.gestionevenement.service.mapper.EvenementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Evenement}.
 */
@Service
@Transactional
public class EvenementServiceImpl implements EvenementService {

    private final Logger log = LoggerFactory.getLogger(EvenementServiceImpl.class);

    private final EvenementRepository evenementRepository;

    private final EvenementMapper evenementMapper;

    public EvenementServiceImpl(EvenementRepository evenementRepository, EvenementMapper evenementMapper) {
        this.evenementRepository = evenementRepository;
        this.evenementMapper = evenementMapper;
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
            .map(evenementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Evenement : {}", id);
        evenementRepository.deleteById(id);
    }
}
