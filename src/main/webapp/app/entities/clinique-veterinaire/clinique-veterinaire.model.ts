import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface ICliniqueVeterinaire {
  id?: number;
  nom?: string | null;
  actif?: boolean | null;
  adresse?: IAdresse | null;
}

export class CliniqueVeterinaire implements ICliniqueVeterinaire {
  constructor(public id?: number, public nom?: string | null, public actif?: boolean | null, public adresse?: IAdresse | null) {
    this.actif = this.actif ?? false;
  }
}

export function getCliniqueVeterinaireIdentifier(cliniqueVeterinaire: ICliniqueVeterinaire): number | undefined {
  return cliniqueVeterinaire.id;
}
