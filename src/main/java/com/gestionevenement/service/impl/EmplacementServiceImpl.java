package com.gestionevenement.service.impl;

import com.gestionevenement.service.EmplacementService;
import com.gestionevenement.domain.Emplacement;
import com.gestionevenement.repository.EmplacementRepository;
import com.gestionevenement.service.dto.EmplacementDTO;
import com.gestionevenement.service.mapper.EmplacementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Emplacement}.
 */
@Service
@Transactional
public class EmplacementServiceImpl implements EmplacementService {

    private final Logger log = LoggerFactory.getLogger(EmplacementServiceImpl.class);

    private final EmplacementRepository emplacementRepository;

    private final EmplacementMapper emplacementMapper;

    public EmplacementServiceImpl(EmplacementRepository emplacementRepository, EmplacementMapper emplacementMapper) {
        this.emplacementRepository = emplacementRepository;
        this.emplacementMapper = emplacementMapper;
    }

    @Override
    public EmplacementDTO save(EmplacementDTO emplacementDTO) {
        log.debug("Request to save Emplacement : {}", emplacementDTO);
        Emplacement emplacement = emplacementMapper.toEntity(emplacementDTO);
        emplacement = emplacementRepository.save(emplacement);
        return emplacementMapper.toDto(emplacement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmplacementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Emplacements");
        return emplacementRepository.findAll(pageable)
            .map(emplacementMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmplacementDTO> findOne(Long id) {
        log.debug("Request to get Emplacement : {}", id);
        return emplacementRepository.findById(id)
            .map(emplacementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Emplacement : {}", id);
        emplacementRepository.deleteById(id);
    }
}
