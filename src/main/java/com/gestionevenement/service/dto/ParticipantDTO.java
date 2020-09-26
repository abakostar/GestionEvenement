package com.gestionevenement.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.gestionevenement.domain.Participant} entity.
 */
public class ParticipantDTO implements Serializable {
    
    private Long id;

    private String nom;

    private String sexe;

    private String telephone;

    private String email;


    private Long villeResidenceId;

    private String villeResidenceNom;

    private Long userId;

    private String userLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getVilleResidenceId() {
        return villeResidenceId;
    }

    public void setVilleResidenceId(Long villeId) {
        this.villeResidenceId = villeId;
    }

    public String getVilleResidenceNom() {
        return villeResidenceNom;
    }

    public void setVilleResidenceNom(String villeNom) {
        this.villeResidenceNom = villeNom;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantDTO)) {
            return false;
        }

        return id != null && id.equals(((ParticipantDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipantDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", villeResidenceId=" + getVilleResidenceId() +
            ", villeResidenceNom='" + getVilleResidenceNom() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
