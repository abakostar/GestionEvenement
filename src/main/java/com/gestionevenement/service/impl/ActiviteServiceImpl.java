package com.gestionevenement.service.impl;

import com.gestionevenement.service.ActiviteService;
import com.gestionevenement.domain.Activite;
import com.gestionevenement.repository.ActiviteRepository;
import com.gestionevenement.service.dto.ActiviteDTO;
import com.gestionevenement.service.mapper.ActiviteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Activite}.
 */
@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService {

    private final Logger log = LoggerFactory.getLogger(ActiviteServiceImpl.class);

    private final ActiviteRepository activiteRepository;

    private final ActiviteMapper activiteMapper;

    public ActiviteServiceImpl(ActiviteRepository activiteRepository, ActiviteMapper activiteMapper) {
        this.activiteRepository = activiteRepository;
        this.activiteMapper = activiteMapper;
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
            .map(activiteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.deleteById(id);
    }
}
