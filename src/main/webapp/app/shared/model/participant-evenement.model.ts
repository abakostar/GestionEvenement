import {IParticipant, Participant} from "./participant.model";

export interface IParticipantEvenement {
  id?: number,
  participant?: IParticipant;
  registered?: boolean;
  evenementId?: number;
  participantId?: number;
}
