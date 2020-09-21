import { Moment } from 'moment';

export interface IActivite {
  id?: number;
  nom?: string;
  description?: string;
  date_activite?: Moment;
  heure_debut?: Moment;
  heure_fin?: Moment;
  etatclos?: boolean;
  evenementCode?: string;
  evenementId?: number;
  emplacementCode?: string;
  emplacementId?: number;
}

export class Activite implements IActivite {
  constructor(
    public id?: number,
    public nom?: string,
    public description?: string,
    public date_activite?: Moment,
    public heure_debut?: Moment,
    public heure_fin?: Moment,
    public etatclos?: boolean,
    public evenementCode?: string,
    public evenementId?: number,
    public emplacementCode?: string,
    public emplacementId?: number
  ) {
    this.etatclos = this.etatclos || false;
  }
}
