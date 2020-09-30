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
 * Criteria class for the {@link com.gestionevenement.domain.InscriptionActivite} entity. This class is used
 * in {@link com.gestionevenement.web.rest.InscriptionActiviteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscription-activites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InscriptionActiviteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter role;

    private LongFilter activiteId;

    private LongFilter participantId;

    public InscriptionActiviteCriteria() {
    }

    public InscriptionActiviteCriteria(InscriptionActiviteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.activiteId = other.activiteId == null ? null : other.activiteId.copy();
        this.participantId = other.participantId == null ? null : other.participantId.copy();
    }

    @Override
    public InscriptionActiviteCriteria copy() {
        return new InscriptionActiviteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRole() {
        return role;
    }

    public void setRole(StringFilter role) {
        this.role = role;
    }

    public LongFilter getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(LongFilter activiteId) {
        this.activiteId = activiteId;
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
        final InscriptionActiviteCriteria that = (InscriptionActiviteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(role, that.role) &&
            Objects.equals(activiteId, that.activiteId) &&
            Objects.equals(participantId, that.participantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        role,
        activiteId,
        participantId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionActiviteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (activiteId != null ? "activiteId=" + activiteId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
            "}";
    }

}
