package com.gestionevenement.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.InscriptionEvenement} entity.
 */
public class InscriptionEvenementDTO implements Serializable {
    
    private Long id;

    private String loginParticipant;

    private String passwordParticipant;


    private Long evenementId;

    private String evenementDescription;

    private Long participantId;

    private String participantFirstName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginParticipant() {
        return loginParticipant;
    }

    public void setLoginParticipant(String loginParticipant) {
        this.loginParticipant = loginParticipant;
    }

    public String getPasswordParticipant() {
        return passwordParticipant;
    }

    public void setPasswordParticipant(String passwordParticipant) {
        this.passwordParticipant = passwordParticipant;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }

    public String getEvenementDescription() {
        return evenementDescription;
    }

    public void setEvenementDescription(String evenementDescription) {
        this.evenementDescription = evenementDescription;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getParticipantFirstName() {
        return participantFirstName;
    }

    public void setParticipantFirstName(String participantFirstName) {
        this.participantFirstName = participantFirstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionEvenementDTO)) {
            return false;
        }

        return id != null && id.equals(((InscriptionEvenementDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionEvenementDTO{" +
            "id=" + getId() +
            ", loginParticipant='" + getLoginParticipant() + "'" +
            ", passwordParticipant='" + getPasswordParticipant() + "'" +
            ", evenementId=" + getEvenementId() +
            ", evenementDescription='" + getEvenementDescription() + "'" +
            ", participantId=" + getParticipantId() +
            ", participantFirstName='" + getParticipantFirstName() + "'" +
            "}";
    }
}
