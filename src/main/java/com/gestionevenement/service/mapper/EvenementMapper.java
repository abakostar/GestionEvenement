package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.EvenementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evenement} and its DTO {@link EvenementDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategorieMapper.class})
public interface EvenementMapper extends EntityMapper<EvenementDTO, Evenement> {

    @Mapping(source = "categorie.id", target = "categorieId")
    @Mapping(source = "categorie.nom", target = "categorieNom")
    EvenementDTO toDto(Evenement evenement);

    @Mapping(source = "categorieId", target = "categorie")
    Evenement toEntity(EvenementDTO evenementDTO);

    default Evenement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Evenement evenement = new Evenement();
        evenement.setId(id);
        return evenement;
    }
}
