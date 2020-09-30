package com.gestionevenement.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.InscriptionActivite} entity.
 */
public class InscriptionActiviteDTO implements Serializable {
    
    private Long id;

    private String role;


    private Long activiteId;

    private String activiteNom;

    private Long participantId;

    private String participantNom;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(Long activiteId) {
        this.activiteId = activiteId;
    }

    public String getActiviteNom() {
        return activiteNom;
    }

    public void setActiviteNom(String activiteNom) {
        this.activiteNom = activiteNom;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getParticipantNom() {
        return participantNom;
    }

    public void setParticipantNom(String participantNom) {
        this.participantNom = participantNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionActiviteDTO)) {
            return false;
        }

        return id != null && id.equals(((InscriptionActiviteDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionActiviteDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", activiteId=" + getActiviteId() +
            ", activiteNom='" + getActiviteNom() + "'" +
            ", participantId=" + getParticipantId() +
            ", participantNom='" + getParticipantNom() + "'" +
            "}";
    }
}
