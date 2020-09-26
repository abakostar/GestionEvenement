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
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.gestionevenement.domain.Activite} entity. This class is used
 * in {@link com.gestionevenement.web.rest.ActiviteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActiviteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter description;

    private BooleanFilter etatclos;

    private LocalDateFilter dateActivite;

    private ZonedDateTimeFilter heureDebut;

    private ZonedDateTimeFilter heureFin;

    private LongFilter evenementId;

    private LongFilter emplacementId;

    public ActiviteCriteria() {
    }

    public ActiviteCriteria(ActiviteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.etatclos = other.etatclos == null ? null : other.etatclos.copy();
        this.dateActivite = other.dateActivite == null ? null : other.dateActivite.copy();
        this.heureDebut = other.heureDebut == null ? null : other.heureDebut.copy();
        this.heureFin = other.heureFin == null ? null : other.heureFin.copy();
        this.evenementId = other.evenementId == null ? null : other.evenementId.copy();
        this.emplacementId = other.emplacementId == null ? null : other.emplacementId.copy();
    }

    @Override
    public ActiviteCriteria copy() {
        return new ActiviteCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getEtatclos() {
        return etatclos;
    }

    public void setEtatclos(BooleanFilter etatclos) {
        this.etatclos = etatclos;
    }

    public LocalDateFilter getDateActivite() {
        return dateActivite;
    }

    public void setDateActivite(LocalDateFilter dateActivite) {
        this.dateActivite = dateActivite;
    }

    public ZonedDateTimeFilter getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(ZonedDateTimeFilter heureDebut) {
        this.heureDebut = heureDebut;
    }

    public ZonedDateTimeFilter getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(ZonedDateTimeFilter heureFin) {
        this.heureFin = heureFin;
    }

    public LongFilter getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(LongFilter evenementId) {
        this.evenementId = evenementId;
    }

    public LongFilter getEmplacementId() {
        return emplacementId;
    }

    public void setEmplacementId(LongFilter emplacementId) {
        this.emplacementId = emplacementId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActiviteCriteria that = (ActiviteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(description, that.description) &&
            Objects.equals(etatclos, that.etatclos) &&
            Objects.equals(dateActivite, that.dateActivite) &&
            Objects.equals(heureDebut, that.heureDebut) &&
            Objects.equals(heureFin, that.heureFin) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(emplacementId, that.emplacementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        description,
        etatclos,
        dateActivite,
        heureDebut,
        heureFin,
        evenementId,
        emplacementId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActiviteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nom != null ? "nom=" + nom + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (etatclos != null ? "etatclos=" + etatclos + ", " : "") +
                (dateActivite != null ? "dateActivite=" + dateActivite + ", " : "") +
                (heureDebut != null ? "heureDebut=" + heureDebut + ", " : "") +
                (heureFin != null ? "heureFin=" + heureFin + ", " : "") +
                (evenementId != null ? "evenementId=" + evenementId + ", " : "") +
                (emplacementId != null ? "emplacementId=" + emplacementId + ", " : "") +
            "}";
    }

}
