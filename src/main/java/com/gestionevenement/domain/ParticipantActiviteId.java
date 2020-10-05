package com.gestionevenement.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

public class ParticipantActiviteId implements Serializable {
    private Long participantId;
    private Long activiteId;

    public ParticipantActiviteId(){}
    public ParticipantActiviteId(Long participantId, Long activiteId){
        this.participantId = participantId;
        this.activiteId = activiteId;
    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantActiviteId that = (ParticipantActiviteId) o;
        return Objects.equals(participantId, that.participantId) &&
            Objects.equals(activiteId, that.activiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, activiteId);
    }
}
