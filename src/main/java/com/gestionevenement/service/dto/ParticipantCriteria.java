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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.gestionevenement.domain.Participant} entity. This class is used
 * in {@link com.gestionevenement.web.rest.ParticipantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /participants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParticipantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sexe;

    private StringFilter telephone;

    private StringFilter login;

    private LongFilter villeId;

    private LongFilter evenementId;

    private LongFilter activiteId;

    public ParticipantCriteria() {
    }

    public ParticipantCriteria(ParticipantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.villeId = other.villeId == null ? null : other.villeId.copy();
        this.evenementId = other.evenementId == null ? null : other.evenementId.copy();
        this.activiteId = other.activiteId == null ? null : other.activiteId.copy();
    }

    @Override
    public ParticipantCriteria copy() {
        return new ParticipantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSexe() {
        return sexe;
    }

    public void setSexe(StringFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public LongFilter getVilleId() {
        return villeId;
    }

    public void setVilleId(LongFilter villeId) {
        this.villeId = villeId;
    }

    public LongFilter getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(LongFilter evenementId) {
        this.evenementId = evenementId;
    }

    public LongFilter getActiviteId() {
        return activiteId;
    }

    public void setActiviteId(LongFilter activiteId) {
        this.activiteId = activiteId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParticipantCriteria that = (ParticipantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(login, that.login) &&
            Objects.equals(villeId, that.villeId) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(activiteId, that.activiteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sexe,
        telephone,
        login,
        villeId,
        evenementId,
        activiteId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (telephone != null ? "telephone=" + telephone + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (villeId != null ? "villeId=" + villeId + ", " : "") +
                (evenementId != null ? "evenementId=" + evenementId + ", " : "") +
                (activiteId != null ? "activiteId=" + activiteId + ", " : "") +
            "}";
    }

}
