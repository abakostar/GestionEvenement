package com.gestionevenement.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.Emplacement} entity.
 */
public class EmplacementDTO implements Serializable {
    
    private Long id;

    private String code;

    private String description;

    private Integer capacite;


    private Long villeId;

    private String villeNom;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public Long getVilleId() {
        return villeId;
    }

    public void setVilleId(Long villeId) {
        this.villeId = villeId;
    }

    public String getVilleNom() {
        return villeNom;
    }

    public void setVilleNom(String villeNom) {
        this.villeNom = villeNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmplacementDTO)) {
            return false;
        }

        return id != null && id.equals(((EmplacementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmplacementDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", capacite=" + getCapacite() +
            ", villeId=" + getVilleId() +
            ", villeNom='" + getVilleNom() + "'" +
            "}";
    }
}
