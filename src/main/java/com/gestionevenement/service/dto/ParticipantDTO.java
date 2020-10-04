package com.gestionevenement.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.gestionevenement.domain.Participant} entity.
 */
public class ParticipantDTO implements Serializable {
    
    private Long id;

    private String nom;

    private String sexe;

    private String telephone;

    private String email;


    private Long villeId;

    private String villeNom;
    private Set<EvenementDTO> evenements = new HashSet<>();
    private Set<ActiviteDTO> activites = new HashSet<>();
    
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<EvenementDTO> getEvenements() {
        return evenements;
    }

    public void setEvenements(Set<EvenementDTO> evenements) {
        this.evenements = evenements;
    }

    public Set<ActiviteDTO> getActivites() {
        return activites;
    }

    public void setActivites(Set<ActiviteDTO> activites) {
        this.activites = activites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantDTO)) {
            return false;
        }

        return id != null && id.equals(((ParticipantDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipantDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", villeId=" + getVilleId() +
            ", villeNom='" + getVilleNom() + "'" +
            ", evenements='" + getEvenements() + "'" +
            ", activites='" + getActivites() + "'" +
            "}";
    }
}
