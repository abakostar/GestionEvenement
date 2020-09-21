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
 * Criteria class for the {@link com.gestionevenement.domain.Ville} entity. This class is used
 * in {@link com.gestionevenement.web.rest.VilleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /villes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VilleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private LongFilter paysId;

    public VilleCriteria() {
    }

    public VilleCriteria(VilleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.paysId = other.paysId == null ? null : other.paysId.copy();
    }

    @Override
    public VilleCriteria copy() {
        return new VilleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNom() {
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public LongFilter getPaysId() {
        return paysId;
    }

    public void setPaysId(LongFilter paysId) {
        this.paysId = paysId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VilleCriteria that = (VilleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(paysId, that.paysId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        paysId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VilleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (paysId != null ? "paysId=" + paysId + ", " : "") +
            "}";
    }

}
