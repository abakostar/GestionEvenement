import { Moment } from 'moment';
import {IParticipantEvenement} from "./participant-evenement.model";

export interface IEvenement {
  id?: number;
  code?: string;
  dateDebut?: Moment;
  dateFin?: Moment;
  description?: string;
  categorieNom?: string;
  categorieId?: number;
  participants?: IParticipantEvenement[]
}

export class Evenement implements IEvenement {
  constructor(
    public id?: number,
    public code?: string,
    public dateDebut?: Moment,
    public dateFin?: Moment,
    public description?: string,
    public categorieNom?: string,
    public categorieId?: number,
    public participants?: IParticipantEvenement[]
  ) {}
}
