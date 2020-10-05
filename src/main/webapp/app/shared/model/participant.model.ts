import { IEvenement } from 'app/shared/model/evenement.model';
import { IActivite } from 'app/shared/model/activite.model';

export interface IParticipant {
  id?: number;
  sexe?: string;
  telephone?: string;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  villeNom?: string;
  villeId?: number;
  evenements?: IEvenement[];
  activites?: IActivite[];
}

export class Participant implements IParticipant {
  constructor(
    public id?: number,
    public sexe?: string,
    public telephone?: string,
    public login?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public password?: string,
    public villeNom?: string,
    public villeId?: number,
    public evenements?: IEvenement[],
    public activites?: IActivite[]
  ) {
  }
}
