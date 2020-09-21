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

    private LocalDateFilter date_activite;

    private ZonedDateTimeFilter heure_debut;

    private ZonedDateTimeFilter heure_fin;

    private BooleanFilter etatclos;

    private LongFilter evenementId;

    private LongFilter emplacementId;

    public ActiviteCriteria() {
    }

    public ActiviteCriteria(ActiviteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.date_activite = other.date_activite == null ? null : other.date_activite.copy();
        this.heure_debut = other.heure_debut == null ? null : other.heure_debut.copy();
        this.heure_fin = other.heure_fin == null ? null : other.heure_fin.copy();
        this.etatclos = other.etatclos == null ? null : other.etatclos.copy();
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

    public LocalDateFilter getDate_activite() {
        return date_activite;
    }

    public void setDate_activite(LocalDateFilter date_activite) {
        this.date_activite = date_activite;
    }

    public ZonedDateTimeFilter getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(ZonedDateTimeFilter heure_debut) {
        this.heure_debut = heure_debut;
    }

    public ZonedDateTimeFilter getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(ZonedDateTimeFilter heure_fin) {
        this.heure_fin = heure_fin;
    }

    public BooleanFilter getEtatclos() {
        return etatclos;
    }

    public void setEtatclos(BooleanFilter etatclos) {
        this.etatclos = etatclos;
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
            Objects.equals(date_activite, that.date_activite) &&
            Objects.equals(heure_debut, that.heure_debut) &&
            Objects.equals(heure_fin, that.heure_fin) &&
            Objects.equals(etatclos, that.etatclos) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(emplacementId, that.emplacementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nom,
        description,
        date_activite,
        heure_debut,
        heure_fin,
        etatclos,
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
                (date_activite != null ? "date_activite=" + date_activite + ", " : "") +
                (heure_debut != null ? "heure_debut=" + heure_debut + ", " : "") +
                (heure_fin != null ? "heure_fin=" + heure_fin + ", " : "") +
                (etatclos != null ? "etatclos=" + etatclos + ", " : "") +
                (evenementId != null ? "evenementId=" + evenementId + ", " : "") +
                (emplacementId != null ? "emplacementId=" + emplacementId + ", " : "") +
            "}";
    }

}
