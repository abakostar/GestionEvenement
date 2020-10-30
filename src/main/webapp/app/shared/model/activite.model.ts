import { Moment } from 'moment';
import { IParticipantActivite } from './participant-activite.model';

export interface IActivite {
  id?: number;
  nom?: string;
  description?: string;
  etatclos?: boolean;
  dateActivite?: Moment;
  heureDebut?: Moment;
  heureFin?: Moment;
  evenementCode?: string;
  evenementId?: number;
  emplacementCode?: string;
  emplacementId?: number;
  participants?: IParticipantActivite[];
}

export class Activite implements IActivite {
  constructor(
    public id?: number,
    public nom?: string,
    public description?: string,
    public etatclos?: boolean,
    public dateActivite?: Moment,
    public heureDebut?: Moment,
    public heureFin?: Moment,
    public evenementCode?: string,
    public evenementId?: number,
    public emplacementCode?: string,
    public emplacementId?: number,
    public participants?: IParticipantActivite[]
  ) {
    this.etatclos = this.etatclos || false;
  }
}
