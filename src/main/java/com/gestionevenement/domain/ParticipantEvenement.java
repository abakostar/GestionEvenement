package com.gestionevenement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Objects;





@Entity(name = "participant_evenement")
@IdClass(ParticipantEvenemrntId.class)
public class ParticipantEvenement {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "evenement_id")
    private Long evenementId;

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
        ParticipantEvenement that = (ParticipantEvenement) o;
        return Objects.equals(participantId, that.participantId) &&
            Objects.equals(evenementId, that.evenementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, evenementId);
    }

}
