export interface IInscriptionActivite {
  id?: number;
  role?: string;
  activiteNom?: string;
  activiteId?: number;
  participantNom?: string;
  participantId?: number;
}

export class InscriptionActivite implements IInscriptionActivite {
  constructor(
    public id?: number,
    public role?: string,
    public activiteNom?: string,
    public activiteId?: number,
    public participantNom?: string,
    public participantId?: number
  ) {}
}
