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
 * Criteria class for the {@link com.gestionevenement.domain.Inscription} entity. This class is used
 * in {@link com.gestionevenement.web.rest.InscriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InscriptionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter role;

    private LongFilter activiteId;

    private LongFilter participantId;

    public InscriptionCriteria() {
    }

    public InscriptionCriteria(InscriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.role = other.role == null ? null : other.role.copy();
        this.activiteId = other.activiteId == null ? null : other.activiteId.copy();
        this.participantId = other.participantId == null ? null : other.participantId.copy();
    }

    @Override
    public InscriptionCriteria copy() {
        return new InscriptionCriteria(this);
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
        final InscriptionCriteria that = (InscriptionCriteria) o;
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
        return "InscriptionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (role != null ? "role=" + role + ", " : "") +
                (activiteId != null ? "activiteId=" + activiteId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
            "}";
    }

}
