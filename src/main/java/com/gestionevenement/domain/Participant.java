package com.gestionevenement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.time.ZonedDateTime;

/**
 * A Participant.
 */
@Entity
@Table(name = "participant")
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "login")
    private String login;

    @ManyToOne
    @JsonIgnoreProperties(value = "participants", allowSetters = true)
    private Ville ville;

    @ManyToMany
    @JoinTable(name = "participant_evenement",
               joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "evenement_id", referencedColumnName = "id"))
    private Set<Evenement> evenements = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "participant_activite",
               joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "activite_id", referencedColumnName = "id"))
    private Set<Activite> activites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSexe() {
        return sexe;
    }

    public Participant sexe(String sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public Participant telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogin() {
        return login;
    }

    public Participant login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Ville getVille() {
        return ville;
    }

    public Participant ville(Ville ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Set<Evenement> getEvenements() {
        return evenements;
    }

    public Participant evenements(Set<Evenement> evenements) {
        this.evenements = evenements;
        return this;
    }

    public Participant addEvenement(Evenement evenement) {
        this.evenements.add(evenement);
        //evenement.getParticipants().add(this);
        return this;
    }

    public Participant removeEvenement(Evenement evenement) {
        this.evenements.remove(evenement);
        //evenement.getParticipants().remove(this);
        return this;
    }

    public void setEvenements(Set<Evenement> evenements) {
        this.evenements = evenements;
    }

    public Set<Activite> getActivites() {
        return activites;
    }

    public Participant activites(Set<Activite> activites) {
        this.activites = activites;
        return this;
    }

    public Participant addActivite(Activite activite) {
        this.activites.add(activite);
        //activite.getParticipants().add(this);
        return this;
    }

    public Participant removeActivite(Activite activite) {
        this.activites.remove(activite);
        //activite.getParticipants().remove(this);
        return this;
    }

    public void setActivites(Set<Activite> activites) {
        this.activites = activites;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participant)) {
            return false;
        }
        return id != null && id.equals(((Participant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participant{" +
            "id=" + getId() +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", login=" + getLogin() +
            "}";
    }
}
