export interface IAdresse {
  id?: number;
  numero?: number;
  rue?: string;
  codePostale?: string;
  ville?: string;
}

export class Adresse implements IAdresse {
  constructor(public id?: number, public numero?: number, public rue?: string, public codePostale?: string, public ville?: string) {}
}

export function getAdresseIdentifier(adresse: IAdresse): number | undefined {
  return adresse.id;
}
