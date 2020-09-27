package com.gestionevenement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Activite activite;

    @ManyToOne
    @JsonIgnoreProperties(value = "inscriptions", allowSetters = true)
    private Participant participant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public Inscription role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Activite getActivite() {
        return activite;
    }

    public Inscription activite(Activite activite) {
        this.activite = activite;
        return this;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Participant getParticipant() {
        return participant;
    }

    public Inscription participant(Participant participant) {
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
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            "}";
    }
}
