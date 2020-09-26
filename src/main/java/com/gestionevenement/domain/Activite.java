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

    @Column(name = "etatclos")
    private Boolean etatclos;

    @Column(name = "date_activite")
    private LocalDate dateActivite;

    @Column(name = "heure_debut")
    private ZonedDateTime heureDebut;

    @Column(name = "heure_fin")
    private ZonedDateTime heureFin;

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

    public LocalDate getDateActivite() {
        return dateActivite;
    }

    public Activite dateActivite(LocalDate dateActivite) {
        this.dateActivite = dateActivite;
        return this;
    }

    public void setDateActivite(LocalDate dateActivite) {
        this.dateActivite = dateActivite;
    }

    public ZonedDateTime getHeureDebut() {
        return heureDebut;
    }

    public Activite heureDebut(ZonedDateTime heureDebut) {
        this.heureDebut = heureDebut;
        return this;
    }

    public void setHeureDebut(ZonedDateTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public ZonedDateTime getHeureFin() {
        return heureFin;
    }

    public Activite heureFin(ZonedDateTime heureFin) {
        this.heureFin = heureFin;
        return this;
    }

    public void setHeureFin(ZonedDateTime heureFin) {
        this.heureFin = heureFin;
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
            ", etatclos='" + isEtatclos() + "'" +
            ", dateActivite='" + getDateActivite() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            "}";
    }
}
