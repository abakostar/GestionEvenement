import {IParticipant} from './participant.model';
import {IActivite} from "./activite.model";

export interface IParticipantActivite {
  participant?: IParticipant;
  activite: IActivite;
  role?: string;
  registered?: boolean;
}
