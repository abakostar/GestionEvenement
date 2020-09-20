export interface ICategorie {
  id?: number;
  code?: string;
  nom?: string;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public code?: string, public nom?: string) {}
}
