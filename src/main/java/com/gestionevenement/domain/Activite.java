package com.gestionevenement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * A Activite.
 */
@Entity
@Table(name = "activite")
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "date_activite")
    private LocalDate date_activite;

    @Column(name = "heure_debut")
    private ZonedDateTime heure_debut;

    @Column(name = "heure_fin")
    private ZonedDateTime heure_fin;

    @Column(name = "etatclos")
    private Boolean etatclos;

    @ManyToOne
    @JsonIgnoreProperties(value = "activites", allowSetters = true)
    private Evenement evenement;

    @ManyToOne
    @JsonIgnoreProperties(value = "activites", allowSetters = true)
    private Emplacement emplacement;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Activite nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public Activite description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate_activite() {
        return date_activite;
    }

    public Activite date_activite(LocalDate date_activite) {
        this.date_activite = date_activite;
        return this;
    }

    public void setDate_activite(LocalDate date_activite) {
        this.date_activite = date_activite;
    }

    public ZonedDateTime getHeure_debut() {
        return heure_debut;
    }

    public Activite heure_debut(ZonedDateTime heure_debut) {
        this.heure_debut = heure_debut;
        return this;
    }

    public void setHeure_debut(ZonedDateTime heure_debut) {
        this.heure_debut = heure_debut;
    }

    public ZonedDateTime getHeure_fin() {
        return heure_fin;
    }

    public Activite heure_fin(ZonedDateTime heure_fin) {
        this.heure_fin = heure_fin;
        return this;
    }

    public void setHeure_fin(ZonedDateTime heure_fin) {
        this.heure_fin = heure_fin;
    }

    public Boolean isEtatclos() {
        return etatclos;
    }

    public Activite etatclos(Boolean etatclos) {
        this.etatclos = etatclos;
        return this;
    }

    public void setEtatclos(Boolean etatclos) {
        this.etatclos = etatclos;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public Activite evenement(Evenement evenement) {
        this.evenement = evenement;
        return this;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public Emplacement getEmplacement() {
        return emplacement;
    }

    public Activite emplacement(Emplacement emplacement) {
        this.emplacement = emplacement;
        return this;
    }

    public void setEmplacement(Emplacement emplacement) {
        this.emplacement = emplacement;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activite)) {
            return false;
        }
        return id != null && id.equals(((Activite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activite{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", date_activite='" + getDate_activite() + "'" +
            ", heure_debut='" + getHeure_debut() + "'" +
            ", heure_fin='" + getHeure_fin() + "'" +
            ", etatclos='" + isEtatclos() + "'" +
            "}";
    }
}
