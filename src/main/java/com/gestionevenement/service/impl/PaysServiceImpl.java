package com.gestionevenement.service.impl;

import com.gestionevenement.service.PaysService;
import com.gestionevenement.domain.Pays;
import com.gestionevenement.repository.PaysRepository;
import com.gestionevenement.service.dto.PaysDTO;
import com.gestionevenement.service.mapper.PaysMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Pays}.
 */
@Service
@Transactional
public class PaysServiceImpl implements PaysService {

    private final Logger log = LoggerFactory.getLogger(PaysServiceImpl.class);

    private final PaysRepository paysRepository;

    private final PaysMapper paysMapper;

    public PaysServiceImpl(PaysRepository paysRepository, PaysMapper paysMapper) {
        this.paysRepository = paysRepository;
        this.paysMapper = paysMapper;
    }

    @Override
    public PaysDTO save(PaysDTO paysDTO) {
        log.debug("Request to save Pays : {}", paysDTO);
        Pays pays = paysMapper.toEntity(paysDTO);
        pays = paysRepository.save(pays);
        return paysMapper.toDto(pays);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaysDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pays");
        return paysRepository.findAll(pageable)
            .map(paysMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaysDTO> findOne(Long id) {
        log.debug("Request to get Pays : {}", id);
        return paysRepository.findById(id)
            .map(paysMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pays : {}", id);
        paysRepository.deleteById(id);
    }
}
