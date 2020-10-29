package com.gestionevenement.service.dto;

public class ParticipantEventDTO {

    private Long id;
    private ParticipantDTO participant;
    private EvenementDTO evenement;
    private boolean registered;

    public ParticipantEventDTO() {

    }


    public ParticipantEventDTO(ParticipantDTO participant, boolean registered) {
        this.participant = participant;
        this.registered = registered;
        this.id = participant.getId();
    }

    public ParticipantEventDTO(EvenementDTO evenement, boolean registered) {
        this.evenement = evenement;
        this.registered = registered;
        this.id = evenement.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }

    public EvenementDTO getEvenement() {
        return evenement;
    }

    public void setEvenement(EvenementDTO evenement) {
        this.evenement = evenement;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }



}
