import { IActivite } from './activite.model';
import { IParticipant } from './participant.model';
export interface IParticipantActivite {
  id?: number;
  activiteId?: number;
  participantId?: number;
  registered?: boolean;
  participant?: IParticipant;
  activite?: IActivite;
  role?: String;
}
