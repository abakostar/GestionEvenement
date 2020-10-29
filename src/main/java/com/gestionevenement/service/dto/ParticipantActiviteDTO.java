package com.gestionevenement.service.dto;

public class ParticipantActiviteDTO {
    private Long id;
    private ParticipantDTO participant;
    private ActiviteDTO activite;
    private String role;
    private boolean registered;

    public ParticipantActiviteDTO() {
    }

    public ParticipantActiviteDTO(ParticipantDTO participant, String role, boolean registered) {
        this.id = participant.getId();
        this.participant = participant;
        this.role = role;
        this.registered = registered;
    }

    public ParticipantActiviteDTO(ActiviteDTO activite, String role, boolean registered) {
        this.id = activite.getId();
        this.activite = activite;
        this.role = role;
        this.registered = registered;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ParticipantDTO getParticipant() { return participant; }
    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant; }
    public String getRole() { return role; }
    public void setRole(String role) {  this.role = role; }

    public ActiviteDTO getActivite() {
        return activite;
    }

    public void setActivite(ActiviteDTO activite) {
        this.activite = activite;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
