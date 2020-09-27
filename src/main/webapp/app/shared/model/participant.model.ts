export interface IParticipant {
  id?: number;
  nom?: string;
  sexe?: string;
  telephone?: string;
  email?: string;
  villeNom?: string;
  villeId?: number;
}

export class Participant implements IParticipant {
  constructor(
    public id?: number,
    public nom?: string,
    public sexe?: string,
    public telephone?: string,
    public email?: string,
    public villeNom?: string,
    public villeId?: number
  ) {}
}