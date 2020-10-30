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

export class ParticipantEvenement implements IParticipantEvenement {
  constructor(public participantId?: number, public evenementId?: number, public registered?: boolean) {}
}
