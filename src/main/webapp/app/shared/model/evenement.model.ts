import { Moment } from 'moment';
import { IActivite } from './activite.model';
import {IParticipant} from "./participant.model";

export interface IEvenement {
  id?: number;
  code?: string;
  dateDebut?: Moment;
  dateFin?: Moment;
  description?: string;
  categorieNom?: string;
  categorieId?: number;
  registered?: boolean;
  participants?: IParticipant[];
  activites?: IActivite[];
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
    public registered?: boolean,
    public participants?: IParticipant[],
    public activites?: IActivite[]
  ) {}
}
