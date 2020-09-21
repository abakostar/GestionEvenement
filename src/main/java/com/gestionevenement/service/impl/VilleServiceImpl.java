package com.gestionevenement.service.impl;

import com.gestionevenement.service.VilleService;
import com.gestionevenement.domain.Ville;
import com.gestionevenement.repository.VilleRepository;
import com.gestionevenement.service.dto.VilleDTO;
import com.gestionevenement.service.mapper.VilleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ville}.
 */
@Service
@Transactional
public class VilleServiceImpl implements VilleService {

    private final Logger log = LoggerFactory.getLogger(VilleServiceImpl.class);

    private final VilleRepository villeRepository;

    private final VilleMapper villeMapper;

    public VilleServiceImpl(VilleRepository villeRepository, VilleMapper villeMapper) {
        this.villeRepository = villeRepository;
        this.villeMapper = villeMapper;
    }

    @Override
    public VilleDTO save(VilleDTO villeDTO) {
        log.debug("Request to save Ville : {}", villeDTO);
        Ville ville = villeMapper.toEntity(villeDTO);
        ville = villeRepository.save(ville);
        return villeMapper.toDto(ville);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VilleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Villes");
        return villeRepository.findAll(pageable)
            .map(villeMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<VilleDTO> findOne(Long id) {
        log.debug("Request to get Ville : {}", id);
        return villeRepository.findById(id)
            .map(villeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ville : {}", id);
        villeRepository.deleteById(id);
    }
}
