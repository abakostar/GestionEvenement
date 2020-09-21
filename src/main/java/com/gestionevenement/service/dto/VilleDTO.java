package com.gestionevenement.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.Ville} entity.
 */
public class VilleDTO implements Serializable {
    
    private Long id;

    private String nom;


    private Long paysId;

    private String paysNom;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getPaysId() {
        return paysId;
    }

    public void setPaysId(Long paysId) {
        this.paysId = paysId;
    }

    public String getPaysNom() {
        return paysNom;
    }

    public void setPaysNom(String paysNom) {
        this.paysNom = paysNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VilleDTO)) {
            return false;
        }

        return id != null && id.equals(((VilleDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VilleDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", paysId=" + getPaysId() +
            ", paysNom='" + getPaysNom() + "'" +
            "}";
    }
}
