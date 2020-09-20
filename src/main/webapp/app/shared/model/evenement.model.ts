import { Moment } from 'moment';

export interface IEvenement {
  id?: number;
  code?: string;
  dateDebut?: Moment;
  dateFin?: Moment;
  description?: string;
  categorieId?: number;
  categorieNom?: string;
}

export class Evenement implements IEvenement {
  constructor(
    public id?: number,
    public code?: string,
    public dateDebut?: Moment,
    public dateFin?: Moment,
    public description?: string,
    public categorieId?: number
  ) {}
}
