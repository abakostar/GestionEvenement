package com.gestionevenement.service.dto;

import com.gestionevenement.domain.User;
// import com.gestionevenement.security.AuthoritiesConstants;
// import com.gestionevenement.web.rest.vm.ManagedUserVM;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A DTO for the {@link com.gestionevenement.domain.Participant} entity.
 */
public class ParticipantDTO implements Serializable {

    private Long id;

    private String sexe;

    private String telephone;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Long villeId;

    private String villeNom;

    private boolean profileCompleted;

    private boolean registered;

    private List<EvenementDTO> evenements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getVilleId() {
        return villeId;
    }

    public void setVilleId(Long villeId) {
        this.villeId = villeId;
    }

    public String getVilleNom() {
        return villeNom;
    }

    public void setVilleNom(String villeNom) {
        this.villeNom = villeNom;
    }

    public List<EvenementDTO> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<EvenementDTO> evenements) {
        this.evenements = evenements;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
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
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", login='" + getLogin() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", villeId=" + getVilleId() +
            ", villeNom='" + getVilleNom() + "'" +
            ", evenements='" + getEvenements() + "'" +
            "}";
    }

    public void setUser(User user) {
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
        setLogin(user.getLogin());
        setId(id == null ? 0L : id);
    }
}
