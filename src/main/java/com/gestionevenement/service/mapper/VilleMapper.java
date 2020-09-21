package com.gestionevenement.service.mapper;


import com.gestionevenement.domain.*;
import com.gestionevenement.service.dto.VilleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ville} and its DTO {@link VilleDTO}.
 */
@Mapper(componentModel = "spring", uses = {PaysMapper.class})
public interface VilleMapper extends EntityMapper<VilleDTO, Ville> {

    @Mapping(source = "pays.id", target = "paysId")
    @Mapping(source = "pays.nom", target = "paysNom")
    VilleDTO toDto(Ville ville);

    @Mapping(source = "paysId", target = "pays")
    Ville toEntity(VilleDTO villeDTO);

    default Ville fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ville ville = new Ville();
        ville.setId(id);
        return ville;
    }
}
