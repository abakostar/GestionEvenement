export interface IInscriptionEvenement {
  id?: number;
  loginParticipant?: string;
  passwordParticipant?: string;
  evenementDescription?: string;
  evenementId?: number;
  participantFirstName?: string;
  participantId?: number;
}

export class InscriptionEvenement implements IInscriptionEvenement {
  constructor(
    public id?: number,
    public loginParticipant?: string,
    public passwordParticipant?: string,
    public evenementDescription?: string,
    public evenementId?: number,
    public participantFirstName?: string,
    public participantId?: number
  ) {}
}
