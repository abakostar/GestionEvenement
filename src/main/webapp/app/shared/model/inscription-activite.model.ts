export interface IInscriptionActivite {
  id?: number;
  role?: string;
  activiteNom?: string;
  activiteId?: number;
  participantFirstName?: string;
  participantId?: number;
}

export class InscriptionActivite implements IInscriptionActivite {
  constructor(
    public id?: number,
    public role?: string,
    public activiteNom?: string,
    public activiteId?: number,
    public participantFirstName?: string,
    public participantId?: number
  ) {}
}
