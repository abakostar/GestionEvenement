export interface IVille {
  id?: number;
  nom?: string;
  paysNom?: string;
  paysId?: number;
}

export class Ville implements IVille {
  constructor(public id?: number, public nom?: string, public paysNom?: string, public paysId?: number) {}
}
