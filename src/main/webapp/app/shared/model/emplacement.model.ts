export interface IEmplacement {
  id?: number;
  code?: string;
  description?: string;
  capacite?: number;
  villeNom?: string;
  villeId?: number;
}

export class Emplacement implements IEmplacement {
  constructor(
    public id?: number,
    public code?: string,
    public description?: string,
    public capacite?: number,
    public villeNom?: string,
    public villeId?: number
  ) {}
}
