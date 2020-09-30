package com.gestionevenement.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gestionevenement.domain.InscriptionEvenement} entity. This class is used
 * in {@link com.gestionevenement.web.rest.InscriptionEvenementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscription-evenements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InscriptionEvenementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter loginParticipant;

    private StringFilter passwordParticipant;

    private LongFilter evenementId;

    private LongFilter participantId;

    public InscriptionEvenementCriteria() {
    }

    public InscriptionEvenementCriteria(InscriptionEvenementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.loginParticipant = other.loginParticipant == null ? null : other.loginParticipant.copy();
        this.passwordParticipant = other.passwordParticipant == null ? null : other.passwordParticipant.copy();
        this.evenementId = other.evenementId == null ? null : other.evenementId.copy();
        this.participantId = other.participantId == null ? null : other.participantId.copy();
    }

    @Override
    public InscriptionEvenementCriteria copy() {
        return new InscriptionEvenementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLoginParticipant() {
        return loginParticipant;
    }

    public void setLoginParticipant(StringFilter loginParticipant) {
        this.loginParticipant = loginParticipant;
    }

    public StringFilter getPasswordParticipant() {
        return passwordParticipant;
    }

    public void setPasswordParticipant(StringFilter passwordParticipant) {
        this.passwordParticipant = passwordParticipant;
    }

    public LongFilter getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(LongFilter evenementId) {
        this.evenementId = evenementId;
    }

    public LongFilter getParticipantId() {
        return participantId;
    }

    public void setParticipantId(LongFilter participantId) {
        this.participantId = participantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InscriptionEvenementCriteria that = (InscriptionEvenementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(loginParticipant, that.loginParticipant) &&
            Objects.equals(passwordParticipant, that.passwordParticipant) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(participantId, that.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        loginParticipant,
        passwordParticipant,
        evenementId,
        participantId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionEvenementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (loginParticipant != null ? "loginParticipant=" + loginParticipant + ", " : "") +
                (passwordParticipant != null ? "passwordParticipant=" + passwordParticipant + ", " : "") +
                (evenementId != null ? "evenementId=" + evenementId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
            "}";
    }

}
