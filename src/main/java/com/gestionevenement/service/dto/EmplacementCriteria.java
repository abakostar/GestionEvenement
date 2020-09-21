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
 * Criteria class for the {@link com.gestionevenement.domain.Emplacement} entity. This class is used
 * in {@link com.gestionevenement.web.rest.EmplacementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /emplacements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmplacementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter description;

    private IntegerFilter capacite;

    private LongFilter villeId;

    public EmplacementCriteria() {
    }

    public EmplacementCriteria(EmplacementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.capacite = other.capacite == null ? null : other.capacite.copy();
        this.villeId = other.villeId == null ? null : other.villeId.copy();
    }

    @Override
    public EmplacementCriteria copy() {
        return new EmplacementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getCapacite() {
        return capacite;
    }

    public void setCapacite(IntegerFilter capacite) {
        this.capacite = capacite;
    }

    public LongFilter getVilleId() {
        return villeId;
    }

    public void setVilleId(LongFilter villeId) {
        this.villeId = villeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmplacementCriteria that = (EmplacementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(description, that.description) &&
            Objects.equals(capacite, that.capacite) &&
            Objects.equals(villeId, that.villeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        description,
        capacite,
        villeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmplacementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (capacite != null ? "capacite=" + capacite + ", " : "") +
                (villeId != null ? "villeId=" + villeId + ", " : "") +
            "}";
    }

}
