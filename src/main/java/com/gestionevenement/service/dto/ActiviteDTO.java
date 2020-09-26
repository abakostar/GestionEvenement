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

    private Boolean etatclos;

    private LocalDate dateActivite;

    private ZonedDateTime heureDebut;

    private ZonedDateTime heureFin;


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

    public Boolean isEtatclos() {
        return etatclos;
    }

    public void setEtatclos(Boolean etatclos) {
        this.etatclos = etatclos;
    }

    public LocalDate getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(LocalDate dateActivite) {
        this.dateActivite = dateActivite;
    }

    public ZonedDateTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(ZonedDateTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public ZonedDateTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(ZonedDateTime heureFin) {
        this.heureFin = heureFin;
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
            ", etatclos='" + isEtatclos() + "'" +
            ", dateActivite='" + getDateActivite() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", evenementId=" + getEvenementId() +
            ", evenementCode='" + getEvenementCode() + "'" +
            ", emplacementId=" + getEmplacementId() +
            ", emplacementCode='" + getEmplacementCode() + "'" +
            "}";
    }
}
