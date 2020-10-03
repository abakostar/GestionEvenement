import { IEvenement } from 'app/shared/model/evenement.model';

export interface IParticipant {
  id?: number;
  nom?: string;
  sexe?: string;
  telephone?: string;
  email?: string;
  villeNom?: string;
  villeId?: number;
  evenements?: IEvenement[];
}

export class Participant implements IParticipant {
  constructor(
    public id?: number,
    public nom?: string,
    public sexe?: string,
    public telephone?: string,
    public email?: string,
    public villeNom?: string,
    public villeId?: number,
    public evenements?: IEvenement[]
  ) {}
}
