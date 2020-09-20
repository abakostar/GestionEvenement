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
 * Criteria class for the {@link com.gestionevenement.domain.Evenement} entity. This class is used
 * in {@link com.gestionevenement.web.rest.EvenementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evenements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EvenementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private ZonedDateTimeFilter dateDebut;

    private ZonedDateTimeFilter dateFin;

    private StringFilter description;

    private LongFilter categorieId;

    public EvenementCriteria() {
    }

    public EvenementCriteria(EvenementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.categorieId = other.categorieId == null ? null : other.categorieId.copy();
    }

    @Override
    public EvenementCriteria copy() {
        return new EvenementCriteria(this);
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

    public ZonedDateTimeFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTimeFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTimeFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTimeFilter dateFin) {
        this.dateFin = dateFin;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(LongFilter categorieId) {
        this.categorieId = categorieId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EvenementCriteria that = (EvenementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(description, that.description) &&
            Objects.equals(categorieId, that.categorieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        code,
        dateDebut,
        dateFin,
        description,
        categorieId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvenementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (categorieId != null ? "categorieId=" + categorieId + ", " : "") +
            "}";
    }

}
