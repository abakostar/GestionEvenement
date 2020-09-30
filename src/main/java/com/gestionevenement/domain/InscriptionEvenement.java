package com.gestionevenement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A InscriptionEvenement.
 */
@Entity
@Table(name = "inscription_evenement")
public class InscriptionEvenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "login_participant")
    private String loginParticipant;

    @Column(name = "password_participant")
    private String passwordParticipant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "inscriptionEvenements", allowSetters = true)
    private Evenement evenement;

    @ManyToOne
    @JsonIgnoreProperties(value = "inscriptionEvenements", allowSetters = true)
    private Participant participant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginParticipant() {
        return loginParticipant;
    }

    public InscriptionEvenement loginParticipant(String loginParticipant) {
        this.loginParticipant = loginParticipant;
        return this;
    }

    public void setLoginParticipant(String loginParticipant) {
        this.loginParticipant = loginParticipant;
    }

    public String getPasswordParticipant() {
        return passwordParticipant;
    }

    public InscriptionEvenement passwordParticipant(String passwordParticipant) {
        this.passwordParticipant = passwordParticipant;
        return this;
    }

    public void setPasswordParticipant(String passwordParticipant) {
        this.passwordParticipant = passwordParticipant;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public InscriptionEvenement evenement(Evenement evenement) {
        this.evenement = evenement;
        return this;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public Participant getParticipant() {
        return participant;
    }

    public InscriptionEvenement participant(Participant participant) {
        this.participant = participant;
        return this;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionEvenement)) {
            return false;
        }
        return id != null && id.equals(((InscriptionEvenement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionEvenement{" +
            "id=" + getId() +
            ", loginParticipant='" + getLoginParticipant() + "'" +
            ", passwordParticipant='" + getPasswordParticipant() + "'" +
            "}";
    }
}
