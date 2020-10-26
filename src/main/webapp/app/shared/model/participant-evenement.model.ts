import { IEvenement } from './evenement.model';
import { IParticipant } from './participant.model';

export interface IParticipantEvenement {
  id?: number;
  participant?: IParticipant;
  registered?: boolean;
  evenementId?: number;
  participantId?: number;
  evenement?: IEvenement;
}
