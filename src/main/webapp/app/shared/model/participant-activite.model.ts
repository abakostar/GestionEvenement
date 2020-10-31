import {IParticipant} from './participant.model';
import {IActivite} from "./activite.model";
import {IEvenement} from "./evenement.model";

export interface IParticipantActivite {
  participant?: IParticipant;
  activite: IActivite;
  evenement?: IEvenement;
  role?: string;
  registered?: boolean;
  evenementId?: number;
  participantId?: number;
  activiteId?: number;
}

export class ParticipantActivite implements IParticipantActivite {
  constructor(public activite: IActivite, public participantId?: number, public activiteId?: number, public registered?: boolean, public role?: string) {}
}
