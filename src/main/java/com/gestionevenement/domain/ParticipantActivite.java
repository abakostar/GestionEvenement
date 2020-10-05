package com.gestionevenement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;

@Entity(name = "participant_activite")
@IdClass(ParticipantActiviteId.class)
public class ParticipantActivite {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "activite_id")
    private Long activiteId;

    private String role;

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(Long activiteId) {
        this.activiteId = activiteId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantActivite that = (ParticipantActivite) o;
        return Objects.equals(participantId, that.participantId) &&
            Objects.equals(activiteId, that.activiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, activiteId);
    }

    public void setParticipantActiviteId(ParticipantActiviteId participantActiviteId) {
        setParticipantId(participantActiviteId.getParticipantId());
        setActiviteId(participantActiviteId.getActiviteId());
    }
}
