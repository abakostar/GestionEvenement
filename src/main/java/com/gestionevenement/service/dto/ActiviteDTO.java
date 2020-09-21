package com.gestionevenement.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.Activite} entity.
 */
public class ActiviteDTO implements Serializable {
    
    private Long id;

    private String nom;

    private String description;

    private LocalDate date_activite;

    private ZonedDateTime heure_debut;

    private ZonedDateTime heure_fin;

    private Boolean etatclos;


    private Long evenementId;

    private String evenementCode;

    private Long emplacementId;

    private String emplacementCode;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate_activite() {
        return date_activite;
    }

    public void setDate_activite(LocalDate date_activite) {
        this.date_activite = date_activite;
    }

    public ZonedDateTime getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(ZonedDateTime heure_debut) {
        this.heure_debut = heure_debut;
    }

    public ZonedDateTime getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(ZonedDateTime heure_fin) {
        this.heure_fin = heure_fin;
    }

    public Boolean isEtatclos() {
        return etatclos;
    }

    public void setEtatclos(Boolean etatclos) {
        this.etatclos = etatclos;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }

    public String getEvenementCode() {
        return evenementCode;
    }

    public void setEvenementCode(String evenementCode) {
        this.evenementCode = evenementCode;
    }

    public Long getEmplacementId() {
        return emplacementId;
    }

    public void setEmplacementId(Long emplacementId) {
        this.emplacementId = emplacementId;
    }

    public String getEmplacementCode() {
        return emplacementCode;
    }

    public void setEmplacementCode(String emplacementCode) {
        this.emplacementCode = emplacementCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActiviteDTO)) {
            return false;
        }

        return id != null && id.equals(((ActiviteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActiviteDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", date_activite='" + getDate_activite() + "'" +
            ", heure_debut='" + getHeure_debut() + "'" +
            ", heure_fin='" + getHeure_fin() + "'" +
            ", etatclos='" + isEtatclos() + "'" +
            ", evenementId=" + getEvenementId() +
            ", evenementCode='" + getEvenementCode() + "'" +
            ", emplacementId=" + getEmplacementId() +
            ", emplacementCode='" + getEmplacementCode() + "'" +
            "}";
    }
}
