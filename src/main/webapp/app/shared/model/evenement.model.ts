import { Moment } from 'moment';
import { IActivite } from './activite.model';
import { IParticipantEvenement } from './participant-evenement.model';

export interface IEvenement {
  id?: number;
  code?: string;
  dateDebut?: Moment;
  dateFin?: Moment;
  description?: string;
  categorieNom?: string;
  categorieId?: number;
  registered?: boolean;
  participants?: IParticipantEvenement[];
  activites?: IActivite[];
  collapsed?: boolean;
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
    public participants?: IParticipantEvenement[],
    public activites?: IActivite[]
  ) {}
}
