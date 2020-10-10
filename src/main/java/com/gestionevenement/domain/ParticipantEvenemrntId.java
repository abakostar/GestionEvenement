package com.gestionevenement.domain;

import java.io.Serializable;
import java.util.Objects;

public class ParticipantEvenemrntId implements Serializable {
    private Long participantId;
    private Long evenementId;

    public ParticipantEvenemrntId(){}

    public ParticipantEvenemrntId(Long participantId, Long evenementId){
        this.participantId = participantId;
        this.evenementId = evenementId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantEvenemrntId that = (ParticipantEvenemrntId) o;
        return Objects.equals(participantId, that.participantId) &&
            Objects.equals(evenementId, that.evenementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, evenementId);
    }

    @Override
    public String toString() {
        return "ParticipantEvenemrntId{" +
            "participantId=" + participantId +
            ", evenementId=" + evenementId +
            '}';
    }
}
