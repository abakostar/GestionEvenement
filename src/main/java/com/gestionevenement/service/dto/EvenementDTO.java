package com.gestionevenement.service.dto;

import com.gestionevenement.domain.Evenement;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the {@link Evenement} entity.
 */
public class EvenementDTO implements Serializable {

    private Long id;

    private String code;

    private ZonedDateTime dateDebut;

    private ZonedDateTime dateFin;

    private String description;


    private Long categorieId;

    private String categorieNom;

    private int nbActivite;

    private List<ParticipantEventDTO> participants;

    private List<ActiviteDTO> activites;

    public int getNbActivite() {
        return nbActivite;
    }

    public void setNbActivite(int nbActivite) {
        this.nbActivite = nbActivite;
    }

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

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }

    public String getCategorieNom() {
        return categorieNom;
    }

    public void setCategorieNom(String categorieNom) {
        this.categorieNom = categorieNom;
    }

    public List<ParticipantEventDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantEventDTO> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvenementDTO)) {
            return false;
        }

        return id != null && id.equals(((EvenementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvenementDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", description='" + getDescription() + "'" +
            ", categorieId=" + getCategorieId() +
            ", categorieNom='" + getCategorieNom() + "'" +
            "}";
    }

    public void setActivites(List<ActiviteDTO> activites) {
        this.activites = activites;
    }

    public List<ActiviteDTO> getActivites() {
        return activites;
    }
}
