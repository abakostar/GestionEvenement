export interface IPays {
  id?: number;
  code?: string;
  nom?: string;
}

export class Pays implements IPays {
  constructor(public id?: number, public code?: string, public nom?: string) {}
}
