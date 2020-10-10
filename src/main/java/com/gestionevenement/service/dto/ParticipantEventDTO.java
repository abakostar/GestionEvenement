package com.gestionevenement.service.dto;

public class ParticipantEventDTO {

    private Long id;
    private ParticipantDTO participant;
    private boolean registered;

    public ParticipantEventDTO(){

    }

    public ParticipantEventDTO(ParticipantDTO participant, boolean registered){
        this.participant = participant;
        this.registered = registered;
        this.id = participant.getId();
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

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

}
