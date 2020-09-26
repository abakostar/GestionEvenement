export interface IParticipant {
  id?: number;
  nom?: string;
  sexe?: string;
  telephone?: string;
  email?: string;
  villeResidenceNom?: string;
  villeResidenceId?: number;
  userLogin?: string;
  userId?: number;
}

export class Participant implements IParticipant {
  constructor(
    public id?: number,
    public nom?: string,
    public sexe?: string,
    public telephone?: string,
    public email?: string,
    public villeResidenceNom?: string,
    public villeResidenceId?: number,
    public userLogin?: string,
    public userId?: number
  ) {}
}
