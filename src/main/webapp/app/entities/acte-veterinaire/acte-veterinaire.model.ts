import { IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';

export interface IActeVeterinaire {
  id?: number;
  libelle?: string | null;
  visiteVeterinaire?: IVisiteVeterinaire | null;
}

export class ActeVeterinaire implements IActeVeterinaire {
  constructor(public id?: number, public libelle?: string | null, public visiteVeterinaire?: IVisiteVeterinaire | null) {}
}

export function getActeVeterinaireIdentifier(acteVeterinaire: IActeVeterinaire): number | undefined {
  return acteVeterinaire.id;
}
