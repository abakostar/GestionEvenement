export interface IInscriptionEvenement {
  id?: number;
  loginParticipant?: string;
  passwordParticipant?: string;
  evenementDescription?: string;
  evenementId?: number;
  participantNom?: string;
  participantId?: number;
}

export class InscriptionEvenement implements IInscriptionEvenement {
  constructor(
    public id?: number,
    public loginParticipant?: string,
    public passwordParticipant?: string,
    public evenementDescription?: string,
    public evenementId?: number,
    public participantNom?: string,
    public participantId?: number
  ) {}
}
